package br.com.ifpb.pweb2.HermesWallet.service;

import br.com.ifpb.pweb2.HermesWallet.exceptions.ComentarioNaoEncontradoException;

import br.com.ifpb.pweb2.HermesWallet.exceptions.TextoVazioException;
import br.com.ifpb.pweb2.HermesWallet.models.Comentario;
import br.com.ifpb.pweb2.HermesWallet.models.Transacao;
import br.com.ifpb.pweb2.HermesWallet.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository _comentarioRepository;

    @Transactional
    public Comentario save(Comentario comentario, Transacao transacao) throws TextoVazioException {
        if (comentario.getTexto() == null || comentario.getTexto().isBlank()){
            throw new TextoVazioException("Texto não pode ser vazio");
        }

        if(comentario.getId()!=null){
            Optional<Comentario> comentarioAntigoOpt =_comentarioRepository.findById(comentario.getId());
            Comentario comentarioAntigo = comentarioAntigoOpt.get();
            comentarioAntigo.setTexto(comentario.getTexto());
            return _comentarioRepository.save(comentario);
        }
        comentario.setTransacao(transacao);
        return _comentarioRepository.save(comentario);
    }

    @Transactional
    public Comentario editComentario(Long id,Comentario comentarioAtt){ //existe null
        comentarioAtt.setId(id);
        return _comentarioRepository.save(comentarioAtt);
    }

    @Transactional
    public void excluirComentario(Long id){
        _comentarioRepository.deleteById(id);
    }

    @Transactional
    public Comentario getById(Long transacaoId) throws ComentarioNaoEncontradoException{
        Optional<Comentario> comentario = _comentarioRepository.findById(transacaoId);
        if(comentario.isPresent()){
            return comentario.get();
        }
        throw new ComentarioNaoEncontradoException("Comentário não encontrado!");
    }
    @Transactional
    public List<Comentario> getAllByTransacaoId(Long transacaoId){
        return _comentarioRepository.findByTransacaoId(transacaoId);
    }
}


