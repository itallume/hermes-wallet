package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class AccountHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany
    private List<Account> account;
}
