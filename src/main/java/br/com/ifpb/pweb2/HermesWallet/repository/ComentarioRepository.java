package br.com.ifpb.pweb2.HermesWallet.repository;

import br.com.ifpb.pweb2.HermesWallet.models.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByTransacaoId(Long id);

}


