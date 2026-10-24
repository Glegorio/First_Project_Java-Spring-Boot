package br.com.glegorio.crudprodutos.dto;

import java.math.BigDecimal;

public record ProdutoAtualizacaoParcial(
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidade
) {
}
