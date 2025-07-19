package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.exceptions.ErroCategoria;
import br.com.ifpb.pweb2.HermesWallet.exceptions.ErroDescricao;
import br.com.ifpb.pweb2.HermesWallet.exceptions.ErroValor;
import br.com.ifpb.pweb2.HermesWallet.models.*;
import br.com.ifpb.pweb2.HermesWallet.repository.ComentarioRepository;
import br.com.ifpb.pweb2.HermesWallet.repository.ContaRepository;
import br.com.ifpb.pweb2.HermesWallet.repository.TransacaoRepository;
import br.com.ifpb.pweb2.HermesWallet.service.AuthService;
import br.com.ifpb.pweb2.HermesWallet.service.ComentarioService;
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
@RequestMapping("/conta/{idConta}/transacoes/{idTransacao}/comentarios")
public class ComentarioController {

    @Autowired
    ComentarioService comentarioService;

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    TransacaoRepository transacaoRepository;

    @Autowired
    TransacaoService _transacaoService;

    @Autowired
    ContaService _contaService;
    @Autowired
    AuthService _authService;
    @Autowired
    private ComentarioRepository comentarioRepository;


    @GetMapping("/form")
    public ModelAndView getForm(@PathVariable(value = "idConta") Long idConta,
                                @PathVariable(value = "idTransacao") Long idTransacao,
                                Comentario comentario,
                                ModelAndView model, RedirectAttributes attr,
                                HttpSession session){



        Optional<Conta> c = _contaService.getContaById(idConta);
        Optional<Transacao> t = _transacaoService.findTransacaoById(idTransacao);

        if (c.isEmpty() || t.isEmpty()){
            attr.addFlashAttribute("erro", "Conta ou transação inexistente");
            model.setViewName("redirect:/conta/list");
            return model;
        }

        Conta conta = c.get();
        Transacao transacao = t.get();

        Correntista correntista = (Correntista) session.getAttribute("usuario");

        if(!_authService.verificarPermissaoConta(correntista, conta)){
            attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta que não te pertence, faça o login novamente");
            model.setViewName("redirect:/login");
            return model;
        }


        comentario.setTransacao(transacao);
        model.addObject("idConta", transacao.getConta().getId());
        model.addObject("idTransacao", comentario.getTransacao().getId());
        model.addObject("transacao", transacao);
        model.addObject("comentario", comentario);
        model.setViewName("comentario/formulario");
        return model;
    }


    @PostMapping
    public ModelAndView save(@PathVariable( value = "idConta") Long idConta,
                             @PathVariable(value = "idTransacao") Long idTransacao,
                             Comentario comentario,
                             ModelAndView model,
                             RedirectAttributes attr,
                             HttpSession session){

        Optional<Conta> c = contaRepository.findById(idConta);
        Optional<Transacao> t = transacaoRepository.findById(idTransacao); //testar



        if (c.isEmpty() || t.isEmpty()){
            attr.addFlashAttribute("erro", "Conta ou transação inexistente");
            model.setViewName("redirect:/conta/list");
            return model;
        }

        Conta conta = c.get();
        Transacao transacao = t.get();



        Correntista correntista = (Correntista) session.getAttribute("usuario");

        if (conta.getCorrentista().getId() != correntista.getId() ){
            attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta que não te pertence, faça o login novamente");
            model.setViewName("redirect:/logout"); //limpa sessão e volta pro login novamente
            return model;
        }


        try {
            comentarioService.save(comentario);
            attr.addFlashAttribute("mensagem", "Comentario criada com sucesso!");
//            model.setViewName("redirect:/conta/" + idConta + "/transacoes");

        } catch  (ErroDescricao e){
            attr.addFlashAttribute("erroTexto", e.getMessage());
            //model.addObject("erroDescricao", e.getMessage());

        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro inesperado");
        }

        if (model.getViewName() == null) {
            model.setViewName("redirect:/conta/" + idConta + "/transacoes/");
        }

        attr.addFlashAttribute("comentario", comentario);
        model.setViewName("redirect:/conta/" + idConta + "/transacoes");
        return model;
    }

    @GetMapping
    public ModelAndView list(@PathVariable( value = "idConta") Long idConta,
                             @PathVariable(value = "idTransacao") Long idTransacao,
                             Comentario comentario,
                             ModelAndView model,
                             RedirectAttributes attr,
                             HttpSession session){


        Correntista correntista = (Correntista) session.getAttribute("usuario");
        Optional<Conta> conta = _contaService.getContaById(idConta);

        if(_authService.verificarPermissaoConta(correntista, conta.get())){
            model.addObject("comentarios", comentarioService.findAllById(idTransacao));
            model.addObject("idTransacao", idTransacao);
            model.setViewName("comentarios/list");
            return model;
        }
        attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta que não te pertence, faça o login novamente");
        model.setViewName("redirect:/login");
        return model;
    }



    @GetMapping("/{id}")
    public ModelAndView getComentario(@PathVariable(value = "idConta") Long idConta,
                                      @PathVariable(value = "idTransacao") Long idTransacao,
                                      @PathVariable(value = "id") Long id,
                             ModelAndView model,
                             RedirectAttributes attr,
                             HttpSession session){

        Optional<Conta> c = contaRepository.findById(idConta);
        Optional<Transacao> t = transacaoRepository.findById(idTransacao);

        if (c.isEmpty()){
            attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta inexistente");
            model.setViewName("redirect:/conta/list");
            return model;
        }

        Conta conta = c.get();
        Transacao transacao = t.get();

        Correntista correntista = (Correntista) session.getAttribute("usuario");

        if (conta.getCorrentista().getId() != correntista.getId() ){
            attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta que não te pertence, faça o login novamente");
            model.setViewName("redirect:/logout"); //limpa sessão e volta pro login novamente
            return model;
        }




        attr.addFlashAttribute("msg", "Comentário acessado com Sucesso!");
        model.addObject("transacao", transacao);
        model.addObject("comentario", comentarioService.findById(id).get());
        model.setViewName("comentario/formulario");
        return model;
    }

    @GetMapping("/{id}/excluir")
    public ModelAndView excluirComentario(@PathVariable(value = "idConta") Long idConta,
                                          @PathVariable(value = "idTransacao") Long idTransacao,
                                          @PathVariable(value = "id") Long id,
                                          ModelAndView model,
                                          RedirectAttributes attr,
                                          HttpSession session){

        try{
            comentarioService.excluirComentario(id);
        }
        catch (Exception e){
            attr.addFlashAttribute("erro", "Erro inesperado");

        }

        attr.addFlashAttribute("msg", "Comentário excluído com Sucesso!");
        model.setViewName("redirect:/conta/" + idConta + "/transacoes/" + idTransacao + "/detalhes");
        return model;
    }
}
