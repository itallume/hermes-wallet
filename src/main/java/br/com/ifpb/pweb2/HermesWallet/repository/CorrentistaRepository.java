package br.com.ifpb.pweb2.HermesWallet.repository;

import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CorrentistaRepository extends JpaRepository<Correntista, Long>{

}
