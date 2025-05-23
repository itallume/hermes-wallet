package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private String numero;
    private LocalDate diaFechamento;

    @Enumerated(EnumType.STRING)
    private TipoConta tipo;

    @ManyToOne
    private Correntista correntista;

    @OneToMany
    private List<Transacao> transaction;
    //TODO correntistas
}
