package br.com.ifpb.pweb2.HermesWallet.service;

import br.com.ifpb.pweb2.HermesWallet.exceptions.CategoriaNaoEncontradaException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.TextoVazioException;
import br.com.ifpb.pweb2.HermesWallet.models.Categoria;
import br.com.ifpb.pweb2.HermesWallet.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository _CategoriaRepository;

    @Transactional
    public Categoria save(Categoria Categoria) throws TextoVazioException {
        if (Categoria.getNome() == null || Categoria.getNome().isBlank()){
            throw new TextoVazioException("Texto não pode ser vazio");
        }

        if(Categoria.getId()!=null){
            Optional<Categoria> CategoriaAntigoOpt =_CategoriaRepository.findById(Categoria.getId());
            Categoria CategoriaAntigo = CategoriaAntigoOpt.get();
            CategoriaAntigo.setNome(Categoria.getNome());
            return _CategoriaRepository.save(Categoria);
        }
        return _CategoriaRepository.save(Categoria);
    }

    @Transactional
    public Categoria editCategoria(Long id,Categoria CategoriaAtt){ //existe null
        CategoriaAtt.setId(id);
        return _CategoriaRepository.save(CategoriaAtt);
    }

    @Transactional
    public void excluirCategoria(Long id){
        _CategoriaRepository.deleteById(id);
    }

    @Transactional
    public Categoria getById(Long transacaoId) throws CategoriaNaoEncontradaException{
        Optional<Categoria> Categoria = _CategoriaRepository.findById(transacaoId);
        if(Categoria.isPresent()){
            return Categoria.get();
        }
        throw new CategoriaNaoEncontradaException("Comentário não encontrado!");
    }
//    @Transactional
//    public List<Categoria> getAllByTransacaoId(Long transacaoId){
//        return _CategoriaRepository.findByTransacaoId(transacaoId);
//    }
}


