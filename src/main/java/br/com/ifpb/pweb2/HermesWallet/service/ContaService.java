package br.com.ifpb.pweb2.HermesWallet.service;

import java.util.List;
import java.util.Optional;

import br.com.ifpb.pweb2.HermesWallet.exceptions.ErroDescricao;
import br.com.ifpb.pweb2.HermesWallet.exceptions.NumeroInvalido;
import br.com.ifpb.pweb2.HermesWallet.exceptions.TipoContaInvalido;
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
    public Conta createConta(Conta contaNova, Correntista correntista) throws ErroDescricao, NumeroInvalido, TipoContaInvalido {

        if (contaNova.getDescricao() == null || contaNova.getDescricao().isBlank()){
            throw new ErroDescricao("Descrição não pode ser vazia");
        }
        if (contaNova.getNumero() == null || contaNova.getNumero().isBlank()){
            throw new NumeroInvalido("Digite um número de conta válido");
        }
        if (contaNova.getTipo() == null){
            throw new TipoContaInvalido("Descrição não pode ser vazia");
        }


        contaNova.setCorrentista(correntista);
        return _contaRepository.save(contaNova);
    }

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

    public Optional<Conta> getContaById(Long contaId) {
        Optional<Conta> conta = _contaRepository.findById(contaId);
        return conta;
    }

}
