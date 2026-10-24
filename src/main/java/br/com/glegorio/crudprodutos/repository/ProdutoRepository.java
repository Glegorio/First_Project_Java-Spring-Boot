package br.com.glegorio.crudprodutos.repository;

import br.com.glegorio.crudprodutos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
