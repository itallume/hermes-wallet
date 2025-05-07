package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant date;
    private String description;
    private double value;
    private String comment;
    @ManyToOne
    private Account account;

    @ManyToOne
    private Category category;

}
