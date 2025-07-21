package br.com.ifpb.pweb2.HermesWallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.ifpb.pweb2.HermesWallet.exceptions.PermissaoInvalidaException;
import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.service.AuthService;
import br.com.ifpb.pweb2.HermesWallet.service.ContaService;
import br.com.ifpb.pweb2.HermesWallet.service.TransacaoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    TransacaoService _transacaoService;

    @Autowired
    AuthService _authService;

    @Autowired
    ContaService _contaService;
    
@GetMapping
public ModelAndView dashboard(@RequestParam(required = false) Long contaId, ModelAndView model, HttpSession session, RedirectAttributes attr) {
    
    Correntista correntista = (Correntista) session.getAttribute("usuario");   
    
    if (contaId != null) {
        try{
            Conta conta = _contaService.getContaById(contaId);
            _authService.verificarPermissaoConta(correntista, conta);
            model.addObject("contas", _contaService.getContasByCorrentista(correntista));
            model.addObject("contaSelecionada", conta);
            model.addObject("transacoes", _transacaoService.getAllByContaId(contaId));        
        }
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
            return model;
        }
        catch(Exception e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/dashboard");
            return model;
        }
    } else {
        model.addObject("contas", _contaService.getContasByCorrentista(correntista));
    }
    
    model.setViewName("home/dashboard");
    return model;
}
}
