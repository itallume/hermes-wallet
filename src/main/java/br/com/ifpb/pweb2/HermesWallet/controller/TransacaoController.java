package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.models.Conta;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.models.Transacao;
import br.com.ifpb.pweb2.HermesWallet.repository.ContaRepository;
import br.com.ifpb.pweb2.HermesWallet.service.ContaService;
import br.com.ifpb.pweb2.HermesWallet.service.TransacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("conta/{idConta}/transacoes")
public class TransacaoController {

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    TransacaoService transacaoService;

    @GetMapping("/form")
    public ModelAndView getForm(Transacao transacao, ModelAndView model, RedirectAttributes attr){
        if (transacao.getConta() == null) {
            transacao.setConta(new Conta());
        }
        model.addObject("transacao", transacao);
        model.setViewName("transacao/formularioTransacao");
        return model;
    }

    @PostMapping
    public ModelAndView save(@PathVariable( value = "idConta") Long id, Transacao transacao, ModelAndView model, RedirectAttributes attr, HttpSession session){

        Optional<Conta> c = contaRepository.findById(id);

        if (c.isEmpty()){
            attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta inexistente");
            model.setViewName("redirect:/conta/list");
            return model;
        }

        Conta conta = c.get();
        Correntista correntista = (Correntista) session.getAttribute("correntista");

        if (conta.getCorrentista().getId() != correntista.getId() ){
            attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta que não te pertence, faça o login novamente");
            model.setViewName("redirect:/logout"); //limpa sessão e volta pro login novamente
            return model;
        }

        try{
            transacaoService.save(transacao);
            attr.addFlashAttribute("mensagem", "Transacao criada com sucesso!");
            model.setViewName("redirect:/conta/" + id + "/transacoes");
        } catch (Exception e) {
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/correntista/form");
        }
        return model;
    }

    @GetMapping
    public ModelAndView list(@PathVariable( value = "idConta") Long id, ModelAndView model){
        model.addObject("transacoes", transacaoService.findAllById(id));
        model.setViewName("transacao/listaTransacao");
        return model;
    }

    @GetMapping("/{id}")
    public ModelAndView getTransacao(@PathVariable( value = "idConta") Long idConta,
                                     @PathVariable("id") Long id,
                                     ModelAndView model,
                                     RedirectAttributes attr,
                                     HttpSession session){

        Optional<Conta> c = contaRepository.findById(id);

        if (c.isEmpty()){
            attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta inexistente");
            model.setViewName("redirect:/conta/list");
            return model;
        }

        Conta conta = c.get();
        Correntista correntista = (Correntista) session.getAttribute("correntista");

        if (conta.getCorrentista().getId() != correntista.getId() ){
            attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta que não te pertence, faça o login novamente");
            model.setViewName("redirect:/logout"); //limpa sessão e volta pro login novamente
            return model;
        }


        model.addObject("transacao", transacaoService.findById(id));
        model.setViewName("transacao/formularioTransacao");
        return model;
    }

}
