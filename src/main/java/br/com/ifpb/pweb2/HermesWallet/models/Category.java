package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean isActive;
    private String nature; //TODO enum pra isso
    private int order;

    @OneToMany
    private Transaction transaction;
}
