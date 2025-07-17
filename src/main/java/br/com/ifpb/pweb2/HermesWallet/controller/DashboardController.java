package br.com.ifpb.pweb2.HermesWallet.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;

import br.com.ifpb.pweb2.HermesWallet.service.ContaService;
import br.com.ifpb.pweb2.HermesWallet.service.TransacaoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    TransacaoService _transacaoService;

    @Autowired
    ContaService _contaService;
    
    @GetMapping
    public ModelAndView dashboard(
            @RequestParam(required = false) Long contaId,
            ModelAndView model, 
            HttpSession session) {
        
        Correntista correntista = (Correntista) session.getAttribute("usuario");
        
        model.addObject("contas", _contaService.getContasByCorrentista(correntista));
        
        if (contaId != null) {
            Optional<Conta> conta = _contaService.getContaById(contaId);
            if (conta.isPresent()) {
                model.addObject("contaSelecionada", conta.get());
                model.addObject("transacoes", _transacaoService.findAllById(contaId));
            }
        }
        
        model.setViewName("home/dashboard");
        return model;
    }
}
