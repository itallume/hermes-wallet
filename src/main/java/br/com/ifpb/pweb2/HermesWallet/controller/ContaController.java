package br.com.ifpb.pweb2.HermesWallet.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.models.TipoConta;
import br.com.ifpb.pweb2.HermesWallet.models.Transacao;
import br.com.ifpb.pweb2.HermesWallet.repository.ContaRepository;
import br.com.ifpb.pweb2.HermesWallet.service.ContaService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/conta")
public class ContaController {

	
    @Autowired
    ContaRepository contaRepository;
    
    
    @Autowired
    ContaService _contaService;
    
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ModelAndView busque(@PathVariable("id") Long id, ModelAndView model, HttpSession session, RedirectAttributes attr ) {
    	   try {
    	        Optional<Conta> c = contaRepository.findById(id);
    	        
    	        if (c.isPresent()) {
    	            model.addObject("conta", c.get());
    	            model.addObject("tiposConta", TipoConta.values()); //  isso evita erro no <select>
    	            attr.addFlashAttribute("msg", "Conta acessada com Sucesso!");
    	            model.setViewName("conta/formulario");
    	        } else {
    	            attr.addFlashAttribute("msg", "Conta não encontrada!");
    	            model.setViewName("redirect:/conta/list"); // ou onde quiser redirecionar
    	        }
    	    } catch (Exception e) {
    	        attr.addFlashAttribute("msg", "Conta Não Acessada!");
    	        attr.addFlashAttribute("erro", e.getMessage());
    	        model.setViewName("redirect:/conta/list");
    	    }

    	    return model;
    	 
    }

    @GetMapping("form")
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
//criar conta agora passa correntista
    @PostMapping("save")
    public ModelAndView save(Conta conta, ModelAndView model, RedirectAttributes attr, HttpSession session) {
        Correntista correntista = (Correntista) session.getAttribute("usuario");
        //faz um teste se tem esse user lauro
        try {
    	attr.addFlashAttribute("msg", "Conta inserida com sucesso!");
        _contaService.createConta(conta,correntista);
        model.setViewName("redirect:/conta/list");
    	} catch (Exception e) {
    		attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/form");
        }
        return model;
    }
}
