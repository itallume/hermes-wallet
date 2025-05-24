package br.com.ifpb.pweb2.HermesWallet.service;

import br.com.ifpb.pweb2.HermesWallet.models.Transacao;
import br.com.ifpb.pweb2.HermesWallet.repository.ContaRepository;
import br.com.ifpb.pweb2.HermesWallet.repository.TransacaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class TransacaoService {

    @Autowired
    private TransacaoRepository _transacaoRepository;

    @Autowired
    private ContaRepository _contaRepository;

    @Transactional
    public Transacao createTransacao(Transacao novaTransacao){
        return _transacaoRepository.save(novaTransacao);
    }
    @Transactional
    public Optional<Transacao> findById(Long contaId){ //se retornar null?
        return _transacaoRepository.findById(contaId);
    }
    @Transactional
    public List<Transacao> findAllById(Long contaId){
        return _transacaoRepository.findByContaId(contaId);
    }
}
