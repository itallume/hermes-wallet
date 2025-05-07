package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private String number;
    private Date closeDay;
    private String type; // Ser enum

    @ManyToOne
    private AccountHolder holder;

    @OneToMany
    private Transaction transaction;
    //TODO correntistas
}
