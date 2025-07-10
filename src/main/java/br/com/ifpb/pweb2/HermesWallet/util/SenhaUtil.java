package br.com.ifpb.pweb2.HermesWallet.util;

import org.mindrot.jbcrypt.BCrypt;

public abstract class SenhaUtil {

    public static String hashSenha(String senhaEmTextoPlano){
        return BCrypt.hashpw(senhaEmTextoPlano, BCrypt.gensalt());
    }

    public static boolean verificarSenha(String senhaEmTextoPlano, String hashSenha){
        if (BCrypt.checkpw(senhaEmTextoPlano, hashSenha)){
            return true;
        }
        return false;
    }

}
