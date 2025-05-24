package br.com.ifpb.pweb2.HermesWallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.TipoConta;
import br.com.ifpb.pweb2.HermesWallet.service.ContaService;



@Controller
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    ContaService _contaService;

    @GetMapping("create")
    public ModelAndView createForm(ModelAndView model) {
        model.addObject("conta", new Conta());
        model.addObject("tiposConta", TipoConta.values());
        model.setViewName("conta/formulario");
        return model;
    }
    
    @GetMapping("list")
    public ModelAndView list( ModelAndView model) {
        model.addObject("contas", _contaService.getContas());
        model.setViewName("conta/lista");
        return model;
    }

    @PostMapping("save")
    public ModelAndView save(Conta conta, ModelAndView model) {
        model.addObject("correntista", _contaService.createConta(conta).getCorrentista());
        model.setViewName("conta/lista");
        return model;
    }
}
