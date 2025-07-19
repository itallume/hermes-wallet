package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.exceptions.ErroCategoria;
import br.com.ifpb.pweb2.HermesWallet.exceptions.ErroDescricao;
import br.com.ifpb.pweb2.HermesWallet.exceptions.ErroValor;
import br.com.ifpb.pweb2.HermesWallet.exceptions.TipoTransacaoInvalido;
import br.com.ifpb.pweb2.HermesWallet.models.*;
import br.com.ifpb.pweb2.HermesWallet.repository.ComentarioRepository;
import br.com.ifpb.pweb2.HermesWallet.repository.ContaRepository;
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

import java.util.Optional;

@Controller
@RequestMapping("conta/{idConta}/transacoes")
public class TransacaoController {

    @Autowired
    ContaRepository contaRepository;

    @Autowired
    ContaService _contaService;
    @Autowired
    AuthService _authService;
    @Autowired
    TransacaoService transacaoService;
    @Autowired
    ComentarioService _comentarioService;
    @Autowired
    ComentarioRepository _comentarioRepository;


    @GetMapping("/form")
    public ModelAndView getForm(@PathVariable(value = "idConta") Long id, Transacao transacao, ModelAndView model, RedirectAttributes attr,HttpSession session){
        Optional<Conta> contaOpt = _contaService.getContaById(id);
        Conta conta = contaOpt.get();
        Correntista correntista = (Correntista) session.getAttribute("usuario");
        
        if(!_authService.verificarPermissaoConta(correntista, conta)){
            attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta que não te pertence, faça o login novamente");
            model.setViewName("redirect:/login");
            return model;
        }
        
        transacao.setConta(conta);
        model.addObject("idConta", conta.getId());
        model.addObject("transacao", transacao);
        model.addObject("categorias", TipoCategoria.values());
        model.addObject("tipoTransacao", TipoTransacao.values());
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
        Correntista correntista = (Correntista) session.getAttribute("usuario");

        if (conta.getCorrentista().getId() != correntista.getId() ){
            attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta que não te pertence, faça o login novamente");
            model.setViewName("redirect:/logout"); //limpa sessão e volta pro login novamente
            return model;
        }


        try {
            transacaoService.save(transacao);
            attr.addFlashAttribute("mensagem", "Transacao criada com sucesso!");
            model.setViewName("redirect:/conta/" + id + "/transacoes");
        } catch (ErroCategoria e) {
            attr.addFlashAttribute("erroCategoria", e.getMessage());
        } catch  (ErroDescricao e){
            attr.addFlashAttribute("erroDescricao", e.getMessage());
        } catch (ErroValor e){
            attr.addFlashAttribute("erroValor", e.getMessage());
        } catch (TipoTransacaoInvalido e){
            attr.addFlashAttribute("tipoTransacaoInvalido", e.getMessage());
        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro inesperado");
        }

        if (model.getViewName() == null) {
            model.setViewName("redirect:/conta/" + id + "/transacoes/form");
        }

        attr.addFlashAttribute("transacao", transacao);
        return model;
    }

    @GetMapping
    public ModelAndView list(@PathVariable( value = "idConta") Long id, ModelAndView model, RedirectAttributes attr, HttpSession session){
        Correntista correntista = (Correntista) session.getAttribute("usuario");
        Optional<Conta> conta = _contaService.getContaById(id);
        Conta c= conta.get();

        if(_authService.verificarPermissaoConta(correntista, conta.get())){
            model.addObject("transacoes", transacaoService.findAllById(id));
            model.addObject("idConta", id);
            //model.addObject("conta", conta);
            model.addObject("descricaoConta", c.getDescricao());
            model.addObject("numeroConta", c.getNumero());
            model.setViewName("transacao/listaTransacao");
            return model;
        }
        attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta que não te pertence, faça o login novamente");
        model.setViewName("redirect:/login");
        return model;
    }



    @GetMapping("/{id}")
    public ModelAndView getTransacao(@PathVariable( value = "idConta") Long idConta,
                                     @PathVariable("id") Long id,
                                     ModelAndView model,
                                     RedirectAttributes attr,
                                     HttpSession session){

        Optional<Conta> c = contaRepository.findById(idConta);

        if (c.isEmpty()){
            attr.addFlashAttribute("erro", "Você tentou executar uma ação de uma conta inexistente");
            model.setViewName("redirect:/conta/list");
            return model;
        }

        Conta conta = c.get();
        Correntista correntista = (Correntista) session.getAttribute("usuario");
        model.addObject("categorias", TipoCategoria.values()); //  isso evita erro no <select>
        attr.addFlashAttribute("msg", "Conta acessada com Sucesso!");
        model.addObject("transacao", transacaoService.findById(id));
        model.setViewName("transacao/formularioTransacao");
        return model;
    }

    @GetMapping("/{id}/detalhes")
    public ModelAndView detalhesTransacao(
            @PathVariable Long idConta,
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes attr,
            ModelAndView model) {

        // valida conta
        Optional<Conta> contaOpt = _contaService.getContaById(idConta);
        if (contaOpt.isEmpty()) {
            attr.addFlashAttribute("erro", "Conta inexistente");
            model.setViewName("redirect:/conta/list");
            return model;
        }
        Conta conta = contaOpt.get();

        // valida transacao
        Optional<Transacao> transacaoOpt = transacaoService.findTransacaoById(id);
        if (transacaoOpt.isEmpty() || !transacaoOpt.get().getConta().getId().equals(idConta)) {
            attr.addFlashAttribute("erro", "Transação não encontrada para esta conta");
            model.setViewName("redirect:/conta/" + idConta + "/transacoes");
            return model;
        }
        Transacao transacao = transacaoOpt.get();

        // verifica permissão
        Correntista user = (Correntista) session.getAttribute("usuario");
        if (!_authService.verificarPermissaoConta(user, conta)) {
            attr.addFlashAttribute("erro", "Ação não autorizada");
            model.setViewName("redirect:/login");
            return model;
        }

        model.addObject("idConta", idConta);
        model.addObject("transacao", transacao);
        model.addObject("idTransacao", transacao.getId());
        model.addObject("descricao", transacao.getDescricao());
        model.addObject("valor", transacao.getValor());
        model.addObject("data", transacao.getData());
        model.addObject("categoria", transacao.getCategoria());
        model.addObject("comentarios", transacao.getComentarios());
        model.setViewName("transacao/transacaoDetalhes");
        return model;
    }
}



