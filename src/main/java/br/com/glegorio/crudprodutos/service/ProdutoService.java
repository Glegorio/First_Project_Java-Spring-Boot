package br.com.glegorio.crudprodutos.service;

import br.com.glegorio.crudprodutos.dto.ProdutoAtualizacaoParcial;
import br.com.glegorio.crudprodutos.dto.ProdutoCadastro;
import br.com.glegorio.crudprodutos.dto.ProdutoResposta;
import br.com.glegorio.crudprodutos.exception.ProdutoNaoEncontradoException;
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

    @Transactional
    public ProdutoResposta buscarPorId(Long id){
        Produto produto = buscarProdutoExistente(id);

        return ProdutoResposta.deProduto(produto);
    }

    @Transactional
    public ProdutoResposta atualizar(Long id, ProdutoCadastro dados){
        Produto produto = buscarProdutoExistente(id);

        produto.setNome(dados.nome());
        produto.setDescricao(dados.descricao());
        produto.setPreco(dados.preco());
        produto.setQuantidade(dados.quantidade());

        Produto produtoAtualizado = produtoRepository.save(produto);

        return ProdutoResposta.deProduto(produtoAtualizado);
    }

    @Transactional
    public void excluir(Long id){
        Produto produto = buscarProdutoExistente(id);

        produtoRepository.delete(produto);
    }

    @Transactional
    public ProdutoResposta atualizarParcialmente(Long id, ProdutoAtualizacaoParcial dados){
        Produto produto = buscarProdutoExistente(id);

        if(dados.nome() != null){
            produto.setNome(dados.nome());
        }

        if(dados.descricao() != null){
            produto.setDescricao(dados.descricao());
        }

        if(dados.preco() != null){
            produto.setPreco(dados.preco());
        }

        if(dados.quantidade() != null){
            produto.setQuantidade(dados.quantidade());
        }

        Produto produtoAtualizado = produtoRepository.save(produto);

        return ProdutoResposta.deProduto(produtoAtualizado);
    }

    private Produto buscarProdutoExistente(Long id){
        return produtoRepository.findById(id)
                .orElseThrow(
                        ()  -> new ProdutoNaoEncontradoException(id)
                );
    }
}
