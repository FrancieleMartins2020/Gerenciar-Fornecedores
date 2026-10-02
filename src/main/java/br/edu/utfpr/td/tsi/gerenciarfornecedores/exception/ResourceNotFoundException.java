package br.edu.utfpr.td.tsi.gerenciarfornecedores.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }
}
