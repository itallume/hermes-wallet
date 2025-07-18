package br.com.ifpb.pweb2.HermesWallet.service;

import br.com.ifpb.pweb2.HermesWallet.exceptions.ErroCategoria;
import br.com.ifpb.pweb2.HermesWallet.exceptions.ErroDescricao;
import br.com.ifpb.pweb2.HermesWallet.exceptions.ErroValor;
import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Transacao;
import br.com.ifpb.pweb2.HermesWallet.repository.ContaRepository;
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

    @Autowired
    private ContaRepository _contaRepository;

    @Transactional
    public Transacao save(Transacao novaTransacao) throws ErroCategoria, ErroDescricao, ErroValor {
        novaTransacao.setData(Instant.now());

        if (novaTransacao.getDescricao() == null || novaTransacao.getDescricao().isBlank()){
            throw new ErroDescricao("Descrição não pode ser vazio");
        }
        if (novaTransacao.getValor() < 0 || novaTransacao.getValor() == 0.0){
            throw new ErroValor("Valor de transação inválido");
        }
        if (novaTransacao.getCategoria() == null || novaTransacao.getCategoria().name().isBlank()){
            throw new ErroCategoria("Categoria inválida");
        }

        return _transacaoRepository.save(novaTransacao);
    }

    @Transactional
    public Transacao editTransacao(Long id,Transacao transacaoAtt){ //existe null
        transacaoAtt.setId(id);
        return _transacaoRepository.save(transacaoAtt);
    }

    @Transactional
    public Optional<Transacao> findById(Long contaId){ //se retornar null?
        return _transacaoRepository.findById(contaId);
    }
    @Transactional
    public List<Transacao> findAllById(Long contaId){
        return _transacaoRepository.findByContaId(contaId);
    }

    public Optional<Transacao> findTransacaoById(Long transacaoId) {
        return _transacaoRepository.findById(transacaoId);
    }
}


