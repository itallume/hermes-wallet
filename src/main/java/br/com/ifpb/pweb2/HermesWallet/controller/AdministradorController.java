package br.com.ifpb.pweb2.HermesWallet.controller;

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
@RequestMapping("/admin")
public class AdministradorController {

    @Autowired
    private CorrentistaService correntistaService;

    @GetMapping("/cadastro/correntista")
    public ModelAndView getForm(Correntista correntista, ModelAndView model){
        model.addObject("correntista", correntista);
        model.setViewName("administrador/cadastroCorrentista");
        return model;
    }

    @PostMapping("/cadastro/correntista")
    public ModelAndView save(Correntista correntista, ModelAndView model, RedirectAttributes attr){
        //TODO regras de negocio
        correntistaService.save(correntista);
        attr.addFlashAttribute("mensagem", "Correntista inserido com sucesso!");
        model.setViewName("redirect:/admin/listagem/correntista");
        return model;
    }

    @GetMapping("/listagem/correntista")
    public ModelAndView list(ModelAndView model){
        model.addObject("correntistas", correntistaService.findAll());
        model.setViewName("administrador/listagemCorrentistas");
        return model;
    }

}
