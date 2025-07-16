package br.com.ifpb.pweb2.HermesWallet.service;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.*;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.repository.CorrentistaRepository;
import br.com.ifpb.pweb2.HermesWallet.util.SenhaUtil;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CorrentistaService {
    @Autowired
    private CorrentistaRepository correntistaRepoitory;

    public Correntista save(Correntista correntista) throws SenhaInvalida, NomeInvalido, CPFEmUso, CPFInvalido, EmailEmUso, EmailInvalido {
        if (correntista.getNome() == null ||correntista.getNome().isBlank()){
            throw new NomeInvalido("Nome não pode ser nulo");
        }

        if (!correntista.isAdmin()){
            try{
                CPFValidator validadorCpf = new CPFValidator();
                validadorCpf.assertValid(correntista.getCpf());
            }catch (Exception e) {
                throw new CPFInvalido("CPF inválido");
            }
        }

        Optional<Correntista> correntistaExistentePorCpf = correntistaRepoitory.findByCpf(correntista.getCpf());
        if (correntistaExistentePorCpf.isPresent()){
            throw new CPFEmUso("CPF já cadastrado");
        }

        EmailValidator validatorEmail = new EmailValidator();
        if (!validatorEmail.isValid(correntista.getEmail(), null)){
            throw new EmailInvalido("Email invalido");
        }

        Optional<Correntista> correntistaExistentePorEmail = correntistaRepoitory.findByEmail(correntista.getEmail());
        if (correntistaExistentePorEmail.isPresent()){
            throw new EmailEmUso("Email já cadastrado");
        }

        if (correntista.getSenha() == null || correntista.getSenha().isBlank() || correntista.getSenha().length() < 8){
            throw new SenhaInvalida("Senha deve ter no mínimo 8 caracteres");
        }
        correntista.setSenha(SenhaUtil.hashSenha(correntista.getSenha()));
        return correntistaRepoitory.save(correntista);
    }

    public List<Correntista> findAll(){
        return correntistaRepoitory.findAll();
    }
}
