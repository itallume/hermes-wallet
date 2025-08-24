package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.context.MessageSource;

import java.time.Instant;
import java.util.List;

@Data
@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO olhar isso aqui sem validação
    private Instant data;

    @NotBlank(message = "{erro.blank}")
    @Size(min = 2, max = 255, message = "{erro.size}")
    private String descricao;

    @NotNull(message = "{erro.null}")
    @NegativeOrZero(message = "{erro.negativeOrZero}")
    private Double valor;

    //TODO olhar isso aqui sem validação
    @OneToMany(mappedBy = "transacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;

    @NotNull(message = "{erro.null}")
    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @NotNull(message = "{erro.null}")
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

    @NotNull(message = "{erro.null}")
    @Enumerated(EnumType.STRING)
    private TipoCategoria categoria;
}
