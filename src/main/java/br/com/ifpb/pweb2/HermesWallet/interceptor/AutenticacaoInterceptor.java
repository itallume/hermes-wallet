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
        HttpSession session = request.getSession(false);

        if (session != null){
            Correntista usuario = (Correntista) session.getAttribute("usuario");

            if (usuario != null){
                String basePath = request.getContextPath();
                String requestPath = request.getRequestURI();

                String relativePath = requestPath.substring(basePath.length());

                boolean requerAdmin = relativePath.startsWith("/correntista");

                if (requerAdmin && !usuario.isAdmin()){
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado.");
                    return false;
                }
                return true;
            }

        }
        String baseUrl = request.getContextPath();
        response.sendRedirect(response.encodeRedirectURL(baseUrl) + "/login");
        return false;
    }
}
