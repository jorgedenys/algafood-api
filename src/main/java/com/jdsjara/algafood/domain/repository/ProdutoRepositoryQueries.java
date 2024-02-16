package com.jdsjara.algafood.domain.repository;

import com.jdsjara.algafood.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {

	FotoProduto save(FotoProduto foto);
	
	void delete(FotoProduto fotoProduto);
	
}