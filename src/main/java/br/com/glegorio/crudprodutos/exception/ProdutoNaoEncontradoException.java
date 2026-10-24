package br.com.glegorio.crudprodutos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProdutoNaoEncontradoException extends RuntimeException {

    public ProdutoNaoEncontradoException(Long id) {
        super("Produto com ID " + id + " não encontrado");
    }
}
