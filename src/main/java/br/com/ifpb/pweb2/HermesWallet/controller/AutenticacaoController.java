package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.repository.CorrentistaRepository;
import br.com.ifpb.pweb2.HermesWallet.util.SenhaUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AutenticacaoController {

    @Autowired
    private CorrentistaRepository correntistaRepository;

    @GetMapping("/login")
    public ModelAndView getForm(ModelAndView model, Correntista c){
        model.addObject("usuario", c);
        model.setViewName("/autenticacao/login");
        return model;
    }

    @PostMapping("/login")
    public ModelAndView authenticate(ModelAndView model, Correntista correntista, HttpSession session, RedirectAttributes attributes){
        Correntista correntistaLogado;
        try {
            correntistaLogado = this.verificaCorrentista(correntista);
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
            return model;
        }

        session.setAttribute("usuario", correntista);
        String destino = correntista.isAdmin() ? "redirect:/correntista" : "redirect:/conta/list";
        model.setViewName(destino);
        return model;
    }

    @GetMapping("/logout")
    public ModelAndView logout(ModelAndView mav, HttpSession session) {
        session.invalidate();
        mav.setViewName("redirect:/login");
        return mav;
    }

    private Correntista verificaCorrentista(Correntista correntista) throws Exception {
        if (correntista.getCpf().isBlank()){
            throw new Exception("Digite um cpf");
        }
        if ( correntista.getSenha().isBlank()){
            throw new Exception("Digite uma senha");
        }

        Optional<Correntista> c = correntistaRepository.findByCpf(correntista.getCpf());
        if (c.isEmpty()) {
            throw new Exception("CPF ou senha inválidos");
        }
        Correntista correntistaEncontrado = c.get();
        if (SenhaUtil.verificarSenha(correntista.getSenha(), correntistaEncontrado.getSenha())){
            return correntistaEncontrado;
        }
        return null;
    }

}
