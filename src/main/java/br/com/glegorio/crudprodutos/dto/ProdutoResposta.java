package br.com.glegorio.crudprodutos.dto;

import br.com.glegorio.crudprodutos.model.Produto;

import java.math.BigDecimal;

public record ProdutoResposta(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer quantidade
) {

    public static ProdutoResposta deProduto(Produto produto){
        return new ProdutoResposta(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade()
        );
    }
}
