package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Transacao;
import br.com.ifpb.pweb2.HermesWallet.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("conta/{idConta}/transacao") //conta?id?/trans
public class TransacaoController {

    @Autowired
    TransacaoService transacaoService;

    @GetMapping("/form")
    public ModelAndView getForm(Transacao transacao, ModelAndView modelAndView, RedirectAttributes attr){
        if (transacao.getConta() == null) {
            transacao.setConta(new Conta());
        }
        modelAndView.addObject("transacao", transacao);
        modelAndView.setViewName("transacao/formularioTransacao");
        return modelAndView;
    }

    @PostMapping()
    public ModelAndView save(@PathVariable( value = "idConta") Long id, Transacao transacao, ModelAndView modelAndView, RedirectAttributes attr){
        transacaoService.createTransacao(transacao);
        attr.addFlashAttribute("mensagem", "Transacao criada com sucesso!");
        modelAndView.setViewName("redirect:/conta/" + id + "/transacao/list");
        return modelAndView;
    }

    @GetMapping("/list")
    public ModelAndView listAll(@PathVariable( value = "idConta") Long id, ModelAndView modelAndView){
        modelAndView.addObject("transacao", transacaoService.findAllById(id));
        modelAndView.setViewName("transacao/list");
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getTransacao(@PathVariable( value = "idConta") Long idConta,
                                     @PathVariable("id") Long id,
                                     ModelAndView modelAndView){
        modelAndView.addObject("transacao", transacaoService.findById(id));
        modelAndView.setViewName("transacao/formularioTransacao");
        return modelAndView;
    }

}
