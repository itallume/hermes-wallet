package br.com.ifpb.pweb2.HermesWallet.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Correntista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{erro.blank}")
    @Size(min = 2, max = 128)
    private String nome;

    @CPF
    @NotBlank(message = "{erro.blank}")
    private String cpf;

    @Email
    private String email;

    @NotBlank(message = "{erro.blank}")
    @Size(min = 6, max = 128)
    private String senha;

    @NotNull(message = "{erro.null}")
    private boolean admin;

    @OneToMany(mappedBy = "correntista", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Conta> contas;
}
