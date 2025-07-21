package br.com.ifpb.pweb2.HermesWallet.service;

import br.com.ifpb.pweb2.HermesWallet.exceptions.FormValidationException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.TransacaoNaoEncontradaException;
import br.com.ifpb.pweb2.HermesWallet.models.Transacao;
import br.com.ifpb.pweb2.HermesWallet.repository.TransacaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository _transacaoRepository;


    @Transactional
    public Transacao save(Transacao novaTransacao) throws FormValidationException {
        FormValidationException validationException = new FormValidationException("Erro de validação");
        novaTransacao.setData(Instant.now());
        if (novaTransacao.getDescricao() == null || novaTransacao.getDescricao().isBlank()){
            validationException.addError("erroDescricao","Descrição não pode ser vazio");
        }
        if (novaTransacao.getValor() < 0 || novaTransacao.getValor() == 0.0){
            validationException.addError("erroValor","Valor de transação inválido");
        } //implementar a pesquisa se a categ existe no enum
        if (novaTransacao.getCategoria() == null || novaTransacao.getCategoria().name().isBlank()){
            validationException.addError("erroCategoria","Categoria inválida");
        } //implementar a pesquisa se o tipo de categ existe no enum
        if (novaTransacao.getTipoTransacao() == null) {
            validationException.addError("tipoTransacaoInvalido","Tipo de transação inválida");
        }
        if (validationException.hasErrors()) {
            throw validationException;
        }

        return _transacaoRepository.save(novaTransacao);
    }

    @Transactional
    public Transacao editTransacao(Long id,Transacao transacaoAtt){ //existe null
        transacaoAtt.setId(id);
        return _transacaoRepository.save(transacaoAtt);
    }

    @Transactional
    public Transacao getById(Long contaId) throws TransacaoNaoEncontradaException{
        Optional<Transacao> transacao = _transacaoRepository.findById(contaId);
        if(transacao.isPresent()){
            return transacao.get();
        }
        throw new TransacaoNaoEncontradaException("Transação não encontrada!");
    }
    
    @Transactional
    public List<Transacao> getAllByContaId(Long contaId){
        return _transacaoRepository.findByContaId(contaId);
    }

}


