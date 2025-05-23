package br.com.ifpb.pweb2.HermesWallet.service;

import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.repository.CorrentistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorrentistaService {
    @Autowired
    private CorrentistaRepository correntistaRepoitory; //TODO verificar isso aqui

    public Correntista save(Correntista correntista){
        return correntistaRepoitory.save(correntista);
    }

    public List<Correntista> findAll(){
        return correntistaRepoitory.findAll();
    }
}
