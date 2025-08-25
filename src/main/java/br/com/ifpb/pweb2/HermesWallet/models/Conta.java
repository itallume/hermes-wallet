package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.context.MessageSource;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{erro.blank}")
    private String descricao;

    @NotBlank(message = "{erro.blank}")
    private String numero;


    @Min(value = 1, message = "{erro.data.min}")
    @Max(value=31, message = "{erro.data.max}")
    private Integer diaFechamento;

    @NotNull(message = "{erro.null}")
    @Enumerated(EnumType.STRING)
    private TipoConta tipo;

    @ManyToOne(fetch = FetchType.EAGER)
    @Valid
    @JoinColumn(name = "correntista_id")
    private Correntista correntista;

    @Valid
    @OneToMany (mappedBy = "conta", cascade = CascadeType.ALL)
    private List<Transacao> transaction;
}
