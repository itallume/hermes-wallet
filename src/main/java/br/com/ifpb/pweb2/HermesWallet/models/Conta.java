package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private String numero;
    private Date diaFechamento;
    private String tipo; // Ser enum

    @ManyToOne
    private Correntista holder;

    @OneToMany
    private List<Transacao> transaction;
    //TODO correntistas
}
