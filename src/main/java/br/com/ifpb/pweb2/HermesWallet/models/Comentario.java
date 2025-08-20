package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{erro.blank}")
    @Size(min = 2, max = 128)
    private String texto;

    
    @ManyToOne
    @Valid
    @JoinColumn(name = "transacao_id")
    private Transacao transacao;

}
