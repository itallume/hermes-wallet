package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.exceptions.ContaNaoEncontradaException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.FormValidationException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.NaoRelacionadoException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.PermissaoInvalidaException;
import br.com.ifpb.pweb2.HermesWallet.exceptions.TransacaoNaoEncontradaException;
import br.com.ifpb.pweb2.HermesWallet.models.*;
import br.com.ifpb.pweb2.HermesWallet.service.AuthService;
import br.com.ifpb.pweb2.HermesWallet.service.ComentarioService;
import br.com.ifpb.pweb2.HermesWallet.service.ContaService;
import br.com.ifpb.pweb2.HermesWallet.service.TransacaoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("conta/{idConta}/transacoes")
public class TransacaoController {


    @Autowired
    ContaService _contaService;

    @Autowired
    AuthService _authService;

    @Autowired
    TransacaoService transacaoService;

    @Autowired
    ComentarioService _comentarioService;


    @GetMapping("/form")
    public ModelAndView getForm(@PathVariable(value = "idConta") Long id, Transacao transacao, ModelAndView model, RedirectAttributes attr,HttpSession session){
        try{
            Conta conta = _contaService.getContaById(id);
            Correntista correntista = (Correntista) session.getAttribute("usuario");
            _authService.verificarPermissaoConta(correntista, conta);
            transacao.setConta(conta);//FIXME INVESTIGAR ISSO AQUI?
            model.addObject("idConta", conta.getId());
            model.addObject("transacao", transacao);
            model.addObject("categorias", TipoCategoria.values());
            model.addObject("tipoTransacao", TipoTransacao.values());
            model.setViewName("transacao/formularioTransacao");
        }
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
        }
        catch(ContaNaoEncontradaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/list");
        }   
        return model;
    }


    @PostMapping
    public ModelAndView save(@PathVariable( value = "idConta") Long id, @RequestParam(value = "comentarioTexto", required = false) String comentarioTexto,Transacao transacao, ModelAndView model, RedirectAttributes attr, HttpSession session){

        try{
            Conta conta = _contaService.getContaById(id);
            Correntista correntista = (Correntista) session.getAttribute("usuario");
            _authService.verificarPermissaoConta(correntista, conta);
            Transacao transacaoSalva = transacaoService.save(transacao);
            if (comentarioTexto != null && !comentarioTexto.trim().isEmpty()) { 
                Comentario comentario = new Comentario();
                comentario.setTexto(comentarioTexto.trim());
                _comentarioService.save(comentario, transacaoSalva);
            }
            
            attr.addFlashAttribute("mensagem", "Transacao criada com sucesso!");
            model.setViewName("redirect:/conta/" + id + "/transacoes");
        }
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
            return model;
        }
        catch (FormValidationException e) {
            for (Map.Entry<String, String> error : e.getErrors().entrySet()) {
                attr.addFlashAttribute(error.getKey(), error.getValue());
            }
            model.setViewName("redirect:/conta/" + id + "/transacoes/form" );
            return model;
        }
        catch(Exception e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/list");
            return model;
        }  

        if (model.getViewName() == null) {
            model.setViewName("redirect:/conta/" + id + "/transacoes");
        }

        attr.addFlashAttribute("transacao", transacao);
        return model;
    }

    @GetMapping
    public ModelAndView list(@PathVariable( value = "idConta") Long id, ModelAndView model, RedirectAttributes attr, HttpSession session){
        //FIXME TEMTAR PASSAR OBJETO CONTA E NÃO OS CAMPOS
        try{
            Correntista correntista = (Correntista) session.getAttribute("usuario");
            Conta conta = _contaService.getContaById(id);
            _authService.verificarPermissaoConta(correntista, conta);

            model.addObject("transacoes", transacaoService.getAllByContaId(id));
            model.addObject("idConta", id);
            //model.addObject("conta", conta);
            model.addObject("descricaoConta", conta.getDescricao());
            model.addObject("numeroConta", conta.getNumero());
            model.setViewName("transacao/listaTransacao");
        }
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
        }
        catch(ContaNaoEncontradaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/list");
        }   
        
        return model;
    }



    @GetMapping("/{id}")
    public ModelAndView getTransacao(@PathVariable( value = "idConta") Long idConta, @PathVariable("id") Long id, ModelAndView model, RedirectAttributes attr, HttpSession session){
        try{
            Conta conta = _contaService.getContaById(idConta);
            Correntista correntista = (Correntista) session.getAttribute("usuario");
            _authService.verificarPermissaoConta(correntista, conta);
            model.addObject("categorias", TipoCategoria.values());
            model.addObject("tipoTransacao", TipoTransacao.values()); //  isso evita erro no <select>
            attr.addFlashAttribute("msg", "Conta acessada com Sucesso!");
            model.addObject("transacao", transacaoService.getById(id));
            model.setViewName("transacao/formularioTransacao");
            
        }
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
        }
        catch(ContaNaoEncontradaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/list");
        }
        catch(TransacaoNaoEncontradaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/transacao/list");
        }     
        return model;
    }

    @GetMapping("/{id}/detalhes")
    public ModelAndView detalhesTransacao(@PathVariable Long idConta, @PathVariable Long id, HttpSession session, RedirectAttributes attr, ModelAndView model) {
            
        //FIXME AJEITAR ESSES 1 MILHAO DE CAMPOSSSSSSSSSSSSSSSSSSSSSSSS
        try{
            Conta conta = _contaService.getContaById(idConta);
            Transacao transacao = transacaoService.getById(id);
            Correntista user = (Correntista) session.getAttribute("usuario");
            _authService.verificarPermissaoConta(user, conta);
            _authService.verificarPermissaoTransacao(conta, transacao.getId());
            model.addObject("idConta", idConta);
            model.addObject("transacao", transacao);
            model.addObject("idTransacao", transacao.getId());
            model.addObject("descricao", transacao.getDescricao());
            model.addObject("valor", transacao.getValor());
            model.addObject("data", transacao.getData());
            model.addObject("categoria", transacao.getCategoria());
            model.addObject("comentarios", transacao.getComentarios());
            model.setViewName("transacao/transacaoDetalhes");
        }
        catch(NaoRelacionadoException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/" + idConta + "/transacoes");
        }
        catch(PermissaoInvalidaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/login");
        }
        catch(ContaNaoEncontradaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/list");
        }
        catch (TransacaoNaoEncontradaException e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/conta/" + idConta + "/transacoes");
        }
        return model;
        
    }
}



