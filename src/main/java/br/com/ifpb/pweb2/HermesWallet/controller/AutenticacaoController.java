package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.repository.CorrentistaRepository;
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

    //TODO add hash
    @PostMapping("/login")
    public ModelAndView authenticate(ModelAndView model, Correntista correntista, HttpSession session, RedirectAttributes attributes){
        if (correntista.getCpf().isBlank() || correntista.getSenha().isBlank()){
            attributes.addFlashAttribute("msg", "Dados inválidos");
            model.setViewName("redirect:/login");
            return model;
        }

        Optional<Correntista> c = correntistaRepository.findByCpfAndSenha(correntista.getCpf(), correntista.getSenha());

        if (c.isEmpty()){
            attributes.addFlashAttribute("msg", "CPF ou senha inválidos");
            model.setViewName("redirect:/login");
            return model;
        }
        correntista = c.get();

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


}
