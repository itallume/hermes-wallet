package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.exceptions.*;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.service.CorrentistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/correntista")
public class CorrentistaController {
    @Autowired
    private CorrentistaService correntistaService;

    @GetMapping("/form")
    public ModelAndView getForm(Correntista correntista, ModelAndView model){
        model.addObject("correntista", correntista);
        model.setViewName("correntista/cadastroCorrentista");
        return model;
    }

    @PostMapping
    public ModelAndView save(Correntista correntista, ModelAndView model, RedirectAttributes attr) {
        try {
            correntistaService.save(correntista);
            attr.addFlashAttribute("msg", "Correntista inserido com sucesso!");
            model.setViewName("redirect:/correntista/list");
        } catch (NomeInvalido e) {
            model.addObject("erroNome", e.getMessage());
        } catch (CPFEmUso | CPFInvalido e) {
            model.addObject("erroCPF", e.getMessage());
        } catch (SenhaInvalida e) {
            model.addObject("erroSenha", e.getMessage());
        } catch (EmailEmUso | EmailInvalido e) {
            model.addObject("erroEmail", e.getMessage());
        } catch (Exception e) {
            model.addObject("erro", "Erro inesperado. Tente novamente.");
        }

        if (model.getViewName() == null) {
            model.setViewName("/correntista/cadastroCorrentista");
        }
        model.addObject("correntista", correntista);
        return model;
    }

    @GetMapping("/list")
    public ModelAndView list(ModelAndView model){
        model.addObject("correntistas", correntistaService.findAll());
        model.setViewName("correntista/listagemCorrentistas");
        return model;
    }

}
