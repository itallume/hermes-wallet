package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.DTO.LoginDTO;
import br.com.ifpb.pweb2.HermesWallet.exceptions.DadosLoginInvalidoException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.LoginOuSenhaInvalidosException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.SenhaInvalidaException;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AutenticacaoController {

    @Autowired
    private AuthService _authService;

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
            Correntista correntista = _authService.obterCorrentistaPeloLogin(loginDTO);
            _authService.verificaCorrentista(loginDTO.senha(), correntista);
            session.setAttribute("usuario", correntista);
            attributes.addFlashAttribute("greeting", "Olá " + correntista.getNome() + "!");

            model.setViewName(
                correntista.isAdmin() ?  "redirect:/correntista/list" : "redirect:/dashboard"
            );
            return model;
        } catch (LoginOuSenhaInvalidosException e) {
            model.addObject("erro", e.getMessage());
        } catch (SenhaInvalidaException e) {
            model.addObject("erroSenha", e.getMessage());
        } catch (DadosLoginInvalidoException e) {
            model.addObject("erroLogin", e.getMessage());
        }
        model.setViewName("/autenticacao/login");
        model.addObject("login", loginDTO);
        return model;
    }

    @GetMapping("/logout")
    public ModelAndView logout(ModelAndView mav, HttpSession session) {
        session.invalidate();
        mav.setViewName("redirect:/login");
        return mav;
    }

}
