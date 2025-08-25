package br.com.ifpb.pweb2.HermesWallet.repository;

import br.com.ifpb.pweb2.HermesWallet.models.Categoria;
import br.com.ifpb.pweb2.HermesWallet.models.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Comentario> findByCategoriaId(Long id);
}
