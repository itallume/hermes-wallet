package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;

@Entity
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany
    private Account account;
}
