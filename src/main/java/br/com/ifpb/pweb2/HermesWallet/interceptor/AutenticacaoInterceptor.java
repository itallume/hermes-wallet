package br.com.ifpb.pweb2.HermesWallet.interceptor;

import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AutenticacaoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        boolean permicao = false;
        HttpSession session = request.getSession(false);

        if (session != null && ((Correntista) session.getAttribute("usuario") != null)){
            permicao = true;
        }
        else {
            String baseUrl = request.getContextPath();
            response.sendRedirect(response.encodeRedirectURL(baseUrl));
            permicao = false;
        }
        return permicao;
    }
}
