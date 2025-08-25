package br.com.ifpb.pweb2.HermesWallet.service;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.ifpb.pweb2.HermesWallet.exceptions.*;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.models.User;
import br.com.ifpb.pweb2.HermesWallet.repository.CorrentistaRepository;
import br.com.ifpb.pweb2.HermesWallet.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    public List<User> findEnabledUsers(){
        return userRepository.findByEnabledTrue();
    }

    public Correntista save(Correntista correntista) throws FormValidationException {
        FormValidationException validationException = new FormValidationException("Erro de validação");

        Optional<Correntista> correntistaExistentePorCpf = correntistaRepoitory.findByCpf(correntista.getCpf());
        if (correntistaExistentePorCpf.isPresent()){
            validationException.addError("erroCPF","CPF já cadastrado");
        }

        Optional<Correntista> correntistaExistentePorEmail = correntistaRepoitory.findByEmail(correntista.getEmail());
        if (correntistaExistentePorEmail.isPresent()){
            validationException.addError("erroEmail","Email já cadastrado");
        }

        if (validationException.hasErrors()) {
            throw validationException;
        }
        return correntistaRepoitory.save(correntista);
    }

    public List<Correntista> findAll(){
        return correntistaRepoitory.findAll();
    }

    public Correntista findByUsername(String username) {
        return correntistaRepoitory.findByUserUsername(username);
    }
}
