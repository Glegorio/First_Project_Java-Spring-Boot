package br.com.glegorio.crudprodutos.controller;

import br.com.glegorio.crudprodutos.dto.ProdutoAtualizacaoParcial;
import org.springframework.web.bind.annotation.PatchMapping;
import br.com.glegorio.crudprodutos.dto.ProdutoCadastro;
import br.com.glegorio.crudprodutos.dto.ProdutoResposta;
import br.com.glegorio.crudprodutos.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Cadastro e consulta de produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um produto")
    public ResponseEntity<ProdutoResposta> cadastrar(
            @RequestBody ProdutoCadastro dados
    ) {
        ProdutoResposta produto = produtoService.cadastrar(dados);

        URI localizacao = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produto.id())
                .toUri();

        return ResponseEntity.created(localizacao).body(produto);
    }

    @GetMapping
    @Operation(summary = "Listar os produtos cadastrados")
    public ResponseEntity<List<ProdutoResposta>> listar(){
        List<ProdutoResposta> produtos = produtoService.listar();

        return ResponseEntity.ok(produtos);
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar um produto pelo ID")
    public ResponseEntity<ProdutoResposta> buscarPorId(
            @PathVariable("id") Long id
    ) {
        ProdutoResposta produto = produtoService.buscarPorId(id);

        return ResponseEntity.ok(produto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar somente os campos informados do produto")
    public ResponseEntity<ProdutoResposta> atualizarParcialmente(
            @PathVariable("id") Long id,
            @RequestBody ProdutoAtualizacaoParcial dados
    ) {
        ProdutoResposta produto = produtoService.atualizarParcialmente(
                id,
                dados
        );

        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um produto")
    public ResponseEntity<Void> excluir(
            @PathVariable("id") Long id
    ) {
        produtoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
