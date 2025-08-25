package br.com.ifpb.pweb2.HermesWallet.service;

import java.util.List;
import java.util.Optional;

import br.com.ifpb.pweb2.HermesWallet.exceptions.ContaNaoEncontradaException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.FormValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.repository.ContaRepository;
import jakarta.transaction.Transactional;

@Service
public class ContaService {
    @Autowired
    private ContaRepository _contaRepository;
    
    @Transactional
    public Conta createConta(Conta contaNova, Correntista correntista)  {

        contaNova.setCorrentista(correntista);
        return _contaRepository.save(contaNova);
    }

    @Transactional
    public List<Conta> getContas() {
        List<Conta> accounts = _contaRepository.findAll();
        return accounts;
    }

    @Transactional
    public List<Conta> getContasByCorrentista(Correntista user) {
        List<Conta> accounts = _contaRepository.findByCorrentistaId(user.getId());
        return accounts;
    }

    public Conta getContaById(Long contaId) throws ContaNaoEncontradaException {
        Optional<Conta> conta = _contaRepository.findById(contaId);
        if(conta.isPresent()){
            return conta.get(); 
        }
        throw new ContaNaoEncontradaException("Conta não encontrada!");
    }

}
