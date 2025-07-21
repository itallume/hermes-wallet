package br.com.ifpb.pweb2.HermesWallet.service;

import br.com.ifpb.pweb2.HermesWallet.DTO.LoginDTO;
import br.com.ifpb.pweb2.HermesWallet.exceptions.DadosLoginInvalidoException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.LoginOuSenhaInvalidosException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.NaoRelacionadoException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.PermissaoInvalidaException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.SenhaInvalidaException;
import br.com.ifpb.pweb2.HermesWallet.models.Comentario;
import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.models.Transacao;
import br.com.ifpb.pweb2.HermesWallet.repository.CorrentistaRepository;
import br.com.ifpb.pweb2.HermesWallet.repository.TransacaoRepository;
import br.com.ifpb.pweb2.HermesWallet.util.SenhaUtil;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    TransacaoRepository _transacaoRepository;

    @Autowired
    CorrentistaRepository _correntistaRepository;

    public void verificaCorrentista(String senhaDigitada, Correntista correntista) throws LoginOuSenhaInvalidosException {

        if (!SenhaUtil.verificarSenha(senhaDigitada, correntista.getSenha())) {
            throw new LoginOuSenhaInvalidosException("Login ou Senha inválidos");
        }
    }

    public void verificarPermissaoConta(Correntista correntista,  Conta conta) throws PermissaoInvalidaException{
        if(correntista.getId()!= conta.getCorrentista().getId()){
            throw new PermissaoInvalidaException("Você tentou executar uma ação de uma conta que não te pertence, faça o login novamente");
        }
        
    }

    public void verificarComentarioTransacao(Comentario comentario, long idTransacao)throws NaoRelacionadoException{
        if(!comentario.getTransacao().getId().equals(idTransacao)){
            throw new NaoRelacionadoException("Comentário não pertence à transação especificada.");
        }
    }

    public Correntista obterCorrentistaPeloLogin(LoginDTO l)
            throws DadosLoginInvalidoException, LoginOuSenhaInvalidosException, SenhaInvalidaException {
        String login = l.login();
        String senha = l.senha();
        if (senha.isBlank() || senha.length() < 8) {
            throw new SenhaInvalidaException("Digite uma senha válida");
        }
        if (login.isBlank()) {
            throw new DadosLoginInvalidoException("Digite um CPF ou Email");
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
            throw new LoginOuSenhaInvalidosException("Login ou Senha inválidos");
        }
        return c.get();
    }

    public void verificarPermissaoTransacao(Conta conta, Long idTransacao) throws NaoRelacionadoException{
        Optional<Transacao> transacaoOpt = _transacaoRepository.findById(idTransacao);
        Transacao transacao = transacaoOpt.get();
        if(conta.getId()!= transacao.getConta().getId()){
            throw new NaoRelacionadoException("Transação não encontrada!");
        }
    }
}
