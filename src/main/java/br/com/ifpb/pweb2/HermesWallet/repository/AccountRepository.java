package br.com.ifpb.pweb2.HermesWallet.repository;

import br.com.ifpb.pweb2.HermesWallet.models.Account;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

    List<Account> findByAccountHolderId(Long id);
    
}
