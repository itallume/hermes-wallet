package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.DTO.LoginDTO;
import br.com.ifpb.pweb2.HermesWallet.exceptions.DadosLoginInvalido;
import br.com.ifpb.pweb2.HermesWallet.exceptions.LoginOuSenhaInvalidos;
import br.com.ifpb.pweb2.HermesWallet.exceptions.SenhaInvalida;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.repository.CorrentistaRepository;
import br.com.ifpb.pweb2.HermesWallet.util.SenhaUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AutenticacaoController {

    @Autowired
    private CorrentistaRepository correntistaRepository;

    @GetMapping("/")
    public ModelAndView redirectLogin(ModelAndView model){
        model.setViewName("redirect:/login");
        return model;
    }

    @GetMapping("/login")
    public ModelAndView getForm(ModelAndView model, LoginDTO loginDTO){
        model.addObject("login", loginDTO);
        model.setViewName("/autenticacao/login");
        return model;
    }

    @PostMapping("/login")
    public ModelAndView authenticate(ModelAndView model, LoginDTO loginDTO, HttpSession session, RedirectAttributes attributes){
        try {
            Correntista correntista = this.obterCorrentistaPeloLogin(loginDTO);
            this.verificaCorrentista(loginDTO.senha(), correntista);
            session.setAttribute("usuario", correntista);
            String destino = correntista.isAdmin() ? "redirect:/correntista" : "redirect:/conta/list";
            model.setViewName(destino);
            return model;
        } catch (LoginOuSenhaInvalidos e) {
            attributes.addFlashAttribute("erro", e.getMessage());
        } catch (SenhaInvalida e) {
            attributes.addFlashAttribute("erroSenha", e.getMessage());
        } catch (DadosLoginInvalido e) {
            attributes.addFlashAttribute("erroLogin", e.getMessage());
        }
        model.setViewName("redirect:/login");
        return model;
    }

    @GetMapping("/logout")
    public ModelAndView logout(ModelAndView mav, HttpSession session) {
        session.invalidate();
        mav.setViewName("redirect:/login");
        return mav;
    }

    private void verificaCorrentista(String senhaDigitada, Correntista correntista) throws LoginOuSenhaInvalidos {

//        Optional<Correntista> c = correntistaRepository.findByEmail(correntista.getEmail());
//        if (c.isEmpty()) {
//            throw new LoginOuSenhaInvalidos("Login ou senha inválidos");
//        }
//        Correntista correntistaEncontrado = c.get();
        if (!SenhaUtil.verificarSenha(senhaDigitada, correntista.getSenha())){
            throw new LoginOuSenhaInvalidos("Login ou Senha inválidos");
        }
    }

    private Correntista obterCorrentistaPeloLogin(LoginDTO l) throws DadosLoginInvalido, LoginOuSenhaInvalidos, SenhaInvalida {
        String login = l.login();
        String senha = l.senha();
        if ( senha.isBlank() || senha.length() < 8){
            throw new SenhaInvalida("Digite uma senha válida");
        }
        if (login.isBlank()){
            throw new DadosLoginInvalido("Digite um CPF ou Email");
        }
        Optional<Correntista> c;

        //é cpf?
        if (login.matches("^\\d{11}$")){
            c = correntistaRepository.findByCpf(login);

            //ent é email
        } else{
            c = correntistaRepository.findByEmail(login);
        }
        if (c.isEmpty()){
            throw new LoginOuSenhaInvalidos("Login ou Senha inválidos");
        }
        return c.get();
    }

}
