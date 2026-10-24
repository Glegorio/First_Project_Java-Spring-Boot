package br.com.glegorio.crudprodutos.dto;

import java.math.BigDecimal;

public record ProdutoCadastro(
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidade
) {
}
