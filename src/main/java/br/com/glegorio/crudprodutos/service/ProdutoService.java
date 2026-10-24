package br.com.glegorio.crudprodutos.service;

import br.com.glegorio.crudprodutos.dto.ProdutoCadastro;
import br.com.glegorio.crudprodutos.dto.ProdutoResposta;
import br.com.glegorio.crudprodutos.model.Produto;
import br.com.glegorio.crudprodutos.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public ProdutoResposta cadastrar(ProdutoCadastro dados){
        Produto produto = new Produto(
                dados.nome(),
                dados.descricao(),
                dados.preco(),
                dados.quantidade()
        );

        Produto produtoSalvo = produtoRepository.save(produto);
        return ProdutoResposta.deProduto(produtoSalvo);
    }

    @Transactional
    public List<ProdutoResposta> listar(){
        List<Produto> produtos = produtoRepository.findAll(
                Sort.by(Sort.Direction.ASC, "id")
        );

        return produtos.stream()
                .map(ProdutoResposta::deProduto)
                .toList();
    }

}
