package br.com.ifpb.pweb2.HermesWallet.config;

import br.com.ifpb.pweb2.HermesWallet.models.Correntista;
import br.com.ifpb.pweb2.HermesWallet.repository.CorrentistaRepository;
import br.com.ifpb.pweb2.HermesWallet.util.SenhaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InicializadorAdmin implements ApplicationRunner {

    @Autowired
    private CorrentistaRepository correntistaRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (correntistaRepository.findByEmail("admin@hermesWallet.com").isEmpty()) {
            Correntista correntista = new Correntista();
            correntista.setNome("Administrador");
            correntista.setEmail("admin@hermesWallet.com");
            correntista.setSenha(SenhaUtil.hashSenha("12345678"));
            correntista.setAdmin(true);

            correntistaRepository.save(correntista);
            System.out.println("Correntista admin inserido com sucesso.");
        } else {
            System.out.println("Correntista admin já existe.");
        }
    }
}

