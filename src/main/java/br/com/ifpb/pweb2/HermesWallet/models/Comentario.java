package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String texto;

    
    @ManyToOne
    @JoinColumn(name = "transacao_id")
    private Transacao transacao;

}
