package br.com.ifpb.pweb2.HermesWallet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/account")
@Controller
public class ContaController {

    @RequestMapping("/main")
    public String accountHomePage(HttpServletRequest request, Model model){
        return "conta";
    }

    
}
