package br.com.ifpb.pweb2.HermesWallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.repository.ContaRepository;
import jakarta.transaction.Transactional;

public class ContaService {
        @Autowired
    private ContaRepository _accountRepository;
    
    @Transactional
    public Conta createAccount(Conta newAccount){
        return _accountRepository.save(newAccount);
    } 

    @Transactional
    public List<Conta> getAccountsByAccountHolder(Correntista user) {
        List<Conta> accounts = _accountRepository.findByAccountHolderId(user.getId());
        return accounts;
    }

}
