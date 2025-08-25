package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.exceptions.FormValidationException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.PermissaoInvalidaException;

import java.util.Map;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.models.TipoConta;
import br.com.ifpb.pweb2.HermesWallet.service.AuthService;
import br.com.ifpb.pweb2.HermesWallet.service.ContaService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    ContaService _contaService;

    @Autowired
    AuthService _authService;
    
    @GetMapping("/{id}")
    public ModelAndView get(@PathVariable("id") Long id, ModelAndView model, RedirectAttributes attr, HttpSession session){
        Correntista correntista = (Correntista) session.getAttribute("usuario");
        try{
            Conta conta = _contaService.getContaById(id);
            _authService.verificarPermissaoConta(correntista, conta);
            model.addObject("conta", conta);
            model.addObject("tiposConta", TipoConta.values());
            attr.addFlashAttribute("msg", "Conta acessada com Sucesso!");
            model.setViewName("conta/formulario");
        }
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
        }
        catch(Exception e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/list");
        }
        return model;
    }

    @GetMapping("form")
    public ModelAndView createForm(ModelAndView model, Conta conta) {
        model.addObject("conta", conta);
        model.addObject("tiposConta", TipoConta.values());
        model.setViewName("conta/formulario");
        return model;
    }
    
    @GetMapping("list")
    public ModelAndView list( ModelAndView model, HttpSession session) {
        Correntista correntista = (Correntista) session.getAttribute("usuario");
        model.addObject("contas", _contaService.getContasByCorrentista(correntista));
        model.setViewName("conta/lista");
        return model;
    }

    @PostMapping("save")
    public ModelAndView save(@Valid Conta conta, BindingResult result, ModelAndView model, RedirectAttributes attr, HttpSession session) {
        Correntista correntista = (Correntista) session.getAttribute("usuario");

        if (result.hasErrors()){
            model.addObject("tiposConta", TipoConta.values());
            model.addObject("conta", conta);
            model.setViewName("conta/formulario");
            return model;
        }
        try {
            _contaService.createConta(conta,correntista);
            _authService.verificarPermissaoConta(correntista, conta);
            attr.addFlashAttribute("msg", "Conta inserida com sucesso!");
            model.setViewName("redirect:/conta/list");
    	} 
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
        }

        if (model.getViewName() == null){
            model.setViewName("conta/list");
        }

        attr.addFlashAttribute("conta", conta);
        return model;
    }
}
