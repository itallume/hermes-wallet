package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.exceptions.FormValidationException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.PermissaoInvalidaException;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public ModelAndView save(Conta conta, ModelAndView model, RedirectAttributes attr, HttpSession session) {
        Correntista correntista = (Correntista) session.getAttribute("usuario");
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
        catch (FormValidationException e) {
            for (Map.Entry<String, String> error : e.getErrors().entrySet()) {
                attr.addFlashAttribute(error.getKey(), error.getValue());
            }
            model.setViewName("redirect:/conta/form");
        }

        if (model.getViewName() == null){
            model.setViewName("conta/list");
        }

        attr.addFlashAttribute("conta", conta);
        return model;
    }
}
