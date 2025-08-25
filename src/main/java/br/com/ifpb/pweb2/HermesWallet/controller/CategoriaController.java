package br.com.ifpb.pweb2.HermesWallet.controller;

import br.com.ifpb.pweb2.HermesWallet.exceptions.TextoVazioException;
import br.com.ifpb.pweb2.HermesWallet.models.Categoria;
import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.models.TipoCategoria;
import br.com.ifpb.pweb2.HermesWallet.service.AuthService;
import br.com.ifpb.pweb2.HermesWallet.service.CategoriaService;
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
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    CategoriaService _categoriaService;

    @Autowired
    AuthService _authService;
    
    @GetMapping("/{id}")
    public ModelAndView get(@PathVariable("id") Long id, ModelAndView model, RedirectAttributes attr, HttpSession session){
        Correntista correntista = (Correntista) session.getAttribute("usuario");
        try{
            Categoria categoria = _categoriaService.getById(id);
//            _authService.verificarPermissaocategoria(correntista, categoria);
            model.addObject("categoria", categoria);
            model.addObject("tiposcategoria", TipoCategoria.values());
            attr.addFlashAttribute("msg", "categoria acessada com Sucesso!");
            model.setViewName("categoria/formulario");
        }
//        catch(PermissaoInvalidaException e){
//            attr.addFlashAttribute("erro", e.getMessage());
//            model.setViewName("redirect:/login");
//        }
        catch(Exception e){
            attr.addFlashAttribute("erro", e.getMessage());
            model.setViewName("redirect:/categoria/list");
        }
        return model;
    }

    @GetMapping("/form")
    public ModelAndView createForm(ModelAndView model, Categoria categoria) {
        model.addObject("categoria", categoria);
        model.addObject("tiposcategoria", TipoCategoria.values());
        model.setViewName("categoria/formulario");
        return model;
    }
    
    @GetMapping("/list")
    public ModelAndView list( ModelAndView model, HttpSession session) {
        Correntista correntista = (Correntista) session.getAttribute("usuario");
        model.addObject("categorias", _categoriaService.getAll());
        model.setViewName("categoria/list");
        return model;
    }

    @PostMapping("/save")
    public ModelAndView save(Categoria categoria, ModelAndView model, RedirectAttributes attr, HttpSession session) {
//        Correntista correntista = (Correntista) session.getAttribute("usuario");
        try {
            _categoriaService.createCategoria(categoria);
//            _authService.verificarPermissaocategoria(correntista, categoria); // FIX THISSSSSSSSSS
            attr.addFlashAttribute("msg", "categoria inserida com sucesso!");
            model.setViewName("redirect:/categoria/list");

//        catch(PermissaoInvalidaException e){
//            attr.addFlashAttribute("erro", e.getMessage());
//            model.setViewName("redirect:/login");
//        }
//        catch (FormValidationException e) {
//            for (Map.Entry<String, String> error : e.getErrors().entrySet()) {
//                attr.addFlashAttribute(error.getKey(), error.getValue());
//            }
//            model.setViewName("redirect:/categoria/form");
        } catch (TextoVazioException e) {
            throw new RuntimeException(e);
        }

        if (model.getViewName() == null){
            model.setViewName("categoria/list");
        }

        attr.addFlashAttribute("categoria", categoria);
        return model;
    }
}
