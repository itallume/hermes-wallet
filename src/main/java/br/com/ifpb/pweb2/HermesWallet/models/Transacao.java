package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date data;
    private String descricao;
    private double valor;
    private String comentario;
    @ManyToOne
    private Conta conta;
    @ManyToOne
    private Categoria categoria;

}
