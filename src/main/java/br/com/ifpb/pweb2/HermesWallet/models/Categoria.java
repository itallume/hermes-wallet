package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
//nao usar ainda
@Data
@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{erro.blank}")
    @Size(min = 2, max = 128)
    private String nome;

    private boolean ativo = true;

    @NotNull(message = "{erro.null}")
    @Enumerated(EnumType.STRING)
    private TipoNatureza natureza;

    @Positive
    private int ordem;

    @OneToMany
    private List<Transacao> transacoes;
}
