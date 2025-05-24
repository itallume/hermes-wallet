package br.com.ifpb.pweb2.HermesWallet.repository;

import br.com.ifpb.pweb2.HermesWallet.models.Transacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByContaId(Long id);

}


