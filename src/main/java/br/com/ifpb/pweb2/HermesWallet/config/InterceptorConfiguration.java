package br.com.ifpb.pweb2.HermesWallet.config;

import br.com.ifpb.pweb2.HermesWallet.interceptor.AutenticacaoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Autowired
    AutenticacaoInterceptor autenticacaoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(autenticacaoInterceptor)
                .addPathPatterns("/correntista/**", "/conta/**")
                .excludePathPatterns("/", "/login", "/images/**");
    }
}
