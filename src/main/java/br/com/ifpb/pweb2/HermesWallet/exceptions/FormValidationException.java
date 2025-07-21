package br.com.ifpb.pweb2.HermesWallet.exceptions;

import java.util.HashMap;
import java.util.Map;


public class FormValidationException extends Exception{
    private Map<String, String> errors = new HashMap<>();

    public FormValidationException(String message) {
        super(message);
    }
    
    public FormValidationException addError(String field, String message) {
        errors.put(field, message);
        return this;
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
