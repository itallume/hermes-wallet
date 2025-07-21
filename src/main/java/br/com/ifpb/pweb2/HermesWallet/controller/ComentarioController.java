package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.exceptions.NaoRelacionadoException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.PermissaoInvalidaException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.TextoVazioException;
import br.com.ifpb.pweb2.HermesWallet.models.*;
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

@Controller
@RequestMapping("/conta/{idConta}/transacoes/{idTransacao}/comentarios")
public class ComentarioController {

    @Autowired
    ComentarioService _comentarioService;

    @Autowired
    TransacaoService _transacaoService;

    @Autowired
    ContaService _contaService;

    @Autowired
    AuthService _authService;

    @GetMapping("/form")
    public ModelAndView getForm(@PathVariable(value = "idConta") Long idConta, @PathVariable(value = "idTransacao") Long idTransacao, Comentario comentario, ModelAndView model, RedirectAttributes attr, HttpSession session){

        Correntista correntista = (Correntista) session.getAttribute("usuario");
        try{
            Conta conta = _contaService.getContaById(idConta);
            Transacao transacao = _transacaoService.getById(idTransacao);
            _authService.verificarPermissaoConta(correntista, conta);
            _authService.verificarPermissaoTransacao(conta, idTransacao);
            comentario.setTransacao(transacao);
            model.addObject("idConta", transacao.getConta().getId());
            model.addObject("idTransacao", comentario.getTransacao().getId());
            model.addObject("transacao", transacao);
            model.addObject("comentario", comentario);
            model.setViewName("comentario/formulario");
        }
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
            return model;
        }
        catch(Exception e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/" + idConta + "/transacoes/" + idTransacao + "/detalhes");
            return model;
        }
        return model;
    }


    @PostMapping
    public ModelAndView save(@PathVariable( value = "idConta") Long idConta, @PathVariable(value = "idTransacao") Long idTransacao, Comentario comentario,ModelAndView model, RedirectAttributes attr, HttpSession session){
        try {
            Conta conta = _contaService.getContaById(idConta);
            Correntista correntista = (Correntista) session.getAttribute("usuario");
            _authService.verificarPermissaoConta(correntista, conta);
            Transacao transacao = _transacaoService.getById(idTransacao);
            _authService.verificarComentarioTransacao(comentario,idTransacao); 
            _comentarioService.save(comentario,transacao);
            attr.addFlashAttribute("comentario", comentario);
            model.setViewName("redirect:/conta/" + idConta + "/transacoes/" + idTransacao + "/detalhes");
        } 
        catch (TextoVazioException e) {
            attr.addFlashAttribute("textoVazio", e.getMessage());
            model.setViewName("redirect:/conta/" + idConta + "/transacoes/" + idTransacao + "/comentarios/form"); 
        } 
        catch (NaoRelacionadoException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/" + idConta + "/transacoes/" + idTransacao);
        } 
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
        } 
        catch (Exception e) {
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/" + idConta + "/transacoes/" + idTransacao + "/comentarios/form");
        }
        return model;
    }

    @GetMapping
    public ModelAndView list(@PathVariable( value = "idConta") Long idConta, @PathVariable(value = "idTransacao") Long idTransacao, Comentario comentario, ModelAndView model, RedirectAttributes attr,HttpSession session){
        Correntista correntista = (Correntista) session.getAttribute("usuario");
        try{
            Conta conta = _contaService.getContaById(idConta);
            _authService.verificarPermissaoConta(correntista, conta);
            _authService.verificarPermissaoTransacao(conta, idTransacao);
            model.addObject("comentarios", _comentarioService.getAllByTransacaoId(idTransacao));
            model.addObject("idTransacao", idTransacao);
            model.setViewName("comentarios/list");
        }
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
            return model;
        }
        catch (Exception e) {
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/" + idConta + "/transacoes/" + idTransacao + "/comentarios/form");
            return model;
        }
        return model;
    }



    @GetMapping("/{id}")
    public ModelAndView getComentario(@PathVariable(value = "idConta") Long idConta, @PathVariable(value = "idTransacao") Long idTransacao, @PathVariable(value = "id") Long id, ModelAndView model,RedirectAttributes attr, HttpSession session){
        try{
            Conta conta = _contaService.getContaById(idConta);
            Transacao transacao = _transacaoService.getById(idTransacao);
            Correntista correntista = (Correntista) session.getAttribute("usuario");
            _authService.verificarPermissaoConta(correntista, conta);
            _authService.verificarPermissaoTransacao(conta, idTransacao);
            attr.addFlashAttribute("msg", "Comentário acessado com Sucesso!");
            model.addObject("transacao", transacao);
            model.addObject("comentario", _comentarioService.getById(id));
            model.setViewName("comentario/formulario");
        }
        catch(NaoRelacionadoException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/" + idConta + "/transacoes");
        }
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
            return model;
        }
        catch (Exception e) {
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/" + idConta + "/transacoes");
            return model;
        }
        return model;
    }

    @GetMapping("/{id}/excluir")
    public ModelAndView excluirComentario(@PathVariable(value = "idConta") Long idConta, @PathVariable(value = "idTransacao") Long idTransacao, @PathVariable(value = "id") Long id, ModelAndView model,RedirectAttributes attr, HttpSession session){

        try{
            _comentarioService.excluirComentario(id);
        }
        catch (Exception e){
            attr.addFlashAttribute("erro", "Erro inesperado");
        }
        attr.addFlashAttribute("msg", "Comentário excluído com Sucesso!");
        model.setViewName("redirect:/conta/" + idConta + "/transacoes/" + idTransacao + "/detalhes");
        return model;
    }
}
