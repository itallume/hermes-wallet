package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
//nao usar ainda
@Data
@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private boolean ativo;
    private String natureza; //TODO enum pra isso
    private int ordem;

    @OneToMany
    private List<Transacao> transacoes;
}
