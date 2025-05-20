package br.com.ifpb.pweb2.HermesWallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.ifpb.pweb2.HermesWallet.models.Account;
import br.com.ifpb.pweb2.HermesWallet.models.AccountHolder;
import br.com.ifpb.pweb2.HermesWallet.repository.AccountRepository;
import jakarta.transaction.Transactional;

public class AccountService {
        @Autowired
    private AccountRepository _accountRepository;
    
    @Transactional
    public Account createAccount(Account newAccount){
        return _accountRepository.save(newAccount);
    } 

    @Transactional
    public List<Account> getAccountsByAccountHolder(AccountHolder user) {
        List<Account> accounts = _accountRepository.findByAccountHolderId(user.getId());
        return accounts;
    }

}
