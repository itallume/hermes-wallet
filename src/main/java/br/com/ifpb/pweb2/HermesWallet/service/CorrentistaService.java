package br.com.ifpb.pweb2.HermesWallet.service;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.repository.CorrentistaRepository;
import br.com.ifpb.pweb2.HermesWallet.util.SenhaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CorrentistaService {
    @Autowired
    private CorrentistaRepository correntistaRepoitory;

    public Correntista save(Correntista correntista) throws Exception {
        CPFValidator validadorCpf = new CPFValidator();
        if (!validadorCpf.isEligible(correntista.getCpf())){
            throw new Exception("CPF inválido");
        }

        Optional<Correntista> correntistaExistente = correntistaRepoitory.findByCpf(correntista.getCpf());
        if (correntistaExistente.isPresent()){
            throw new Exception("CPF já cadastrado");
        }

        if (correntista.getNome().isBlank()){
            throw new Exception("Nome não pode ser nulo");
        }

        if (correntista.getSenha().isBlank() || correntista.getSenha().length() < 8){
            throw new Exception("Senha deve ter no mínimo 8 caracteres");
        }
        correntista.setSenha(SenhaUtil.hashSenha(correntista.getSenha()));
        return correntistaRepoitory.save(correntista);
    }

    public List<Correntista> findAll(){
        return correntistaRepoitory.findAll();
    }
}
