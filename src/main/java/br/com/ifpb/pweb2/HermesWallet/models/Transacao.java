package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant data;
    private String descricao;
    private double valor;


    @OneToMany
    private List<Comentario> comentarios;
    
    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

    @Enumerated(EnumType.STRING)
    private TipoCategoria categoria;
}
