package br.com.ifpb.pweb2.HermesWallet.repository;

import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long>{

    List<Conta> findByCorrentistaId(Long id);
    
}
