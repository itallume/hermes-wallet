package br.com.ifpb.pweb2.HermesWallet.service;


import br.com.ifpb.pweb2.HermesWallet.exceptions.ErroDescricao;


import br.com.ifpb.pweb2.HermesWallet.models.Comentario;
import br.com.ifpb.pweb2.HermesWallet.repository.ComentarioRepository;
import br.com.ifpb.pweb2.HermesWallet.repository.ContaRepository;
import br.com.ifpb.pweb2.HermesWallet.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository _comentarioRepository;
    
    @Autowired
    private TransacaoRepository _transacaoRepository;

    @Autowired
    private ContaRepository _contaRepository;

    @Transactional
    public Comentario save(Comentario novoComentario) throws ErroDescricao {

        if (novoComentario.getTexto() == null || novoComentario.getTexto().isBlank()){
            throw new ErroDescricao("Texto não pode ser vazio"); //criar exceção personalizada??
        }

        return _comentarioRepository.save(novoComentario);
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
    public Optional<Comentario> findById(Long transacaoId){ //se retornar null?
        return _comentarioRepository.findById(transacaoId);
    }
    @Transactional
    public List<Comentario> findAllById(Long transacaoId){
        return _comentarioRepository.findByTransacaoId(transacaoId);
    }
}


