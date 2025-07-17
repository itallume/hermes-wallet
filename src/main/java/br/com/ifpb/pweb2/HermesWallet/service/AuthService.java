package br.com.ifpb.pweb2.HermesWallet.service;

import br.com.ifpb.pweb2.HermesWallet.DTO.LoginDTO;
import br.com.ifpb.pweb2.HermesWallet.exceptions.DadosLoginInvalido;
import br.com.ifpb.pweb2.HermesWallet.exceptions.LoginOuSenhaInvalidos;
import br.com.ifpb.pweb2.HermesWallet.exceptions.SenhaInvalida;
import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.models.Transacao;
import br.com.ifpb.pweb2.HermesWallet.repository.CorrentistaRepository;
import br.com.ifpb.pweb2.HermesWallet.util.SenhaUtil;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    CorrentistaRepository _correntistaRepository;

    public void verificaCorrentista(String senhaDigitada, Correntista correntista) throws LoginOuSenhaInvalidos {

        if (!SenhaUtil.verificarSenha(senhaDigitada, correntista.getSenha())) {
            throw new LoginOuSenhaInvalidos("Login ou Senha inválidos");
        }
    }

    public boolean verificarPermissaoConta(Correntista correntista,  Conta conta){
        if(correntista.getId()!= conta.getCorrentista().getId()){
            return false;
        }
        return true;
    }

    public Correntista obterCorrentistaPeloLogin(LoginDTO l)
            throws DadosLoginInvalido, LoginOuSenhaInvalidos, SenhaInvalida {
        String login = l.login();
        String senha = l.senha();
        if (senha.isBlank() || senha.length() < 8) {
            throw new SenhaInvalida("Digite uma senha válida");
        }
        if (login.isBlank()) {
            throw new DadosLoginInvalido("Digite um CPF ou Email");
        }
        Optional<Correntista> c;

        // é cpf?
        if (login.matches("^\\d{11}$")) {
            c = _correntistaRepository.findByCpf(login);

            // ent é email
        } else {
            c = _correntistaRepository.findByEmail(login);
        }
        if (c.isEmpty()) {
            throw new LoginOuSenhaInvalidos("Login ou Senha inválidos");
        }
        return c.get();
    }
}
