package br.com.ifpb.pweb2.HermesWallet.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.repository.ContaRepository;
import br.com.ifpb.pweb2.HermesWallet.repository.CorrentistaRepository;
import jakarta.transaction.Transactional;

@Service
public class ContaService {
    @Autowired
    private ContaRepository _contaRepository;
    
    @Autowired
    private CorrentistaRepository _correntistaRepository;


    //TODO remover gambiarra do correntista
    @Transactional
    public Conta createConta(Conta contaNova, Correntista correntista){
        contaNova.setCorrentista(correntista);
        return _contaRepository.save(contaNova);
    }
//    public Conta createConta(Conta contaNova){
//        Optional<Correntista> c = _correntistaRepository.findById(1233L);
//        if(c.isPresent()){
//            contaNova.setCorrentista(c.get());
//        }
//        return _contaRepository.save(contaNova);
//    }

    @Transactional
    public List<Conta> getContas() {
        List<Conta> accounts = _contaRepository.findAll();
        return accounts;
    }

    @Transactional
    public List<Conta> getContasByCorrentista(Correntista user) {
        List<Conta> accounts = _contaRepository.findByCorrentistaId(user.getId());
        return accounts;
    }

}
