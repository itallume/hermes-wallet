package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant data;
    private String descricao;
    private double valor;
    private String comentario;
    
    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

    @Enumerated(EnumType.STRING)
    private TipoCategoria categoria;
}
