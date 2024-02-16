package com.jdsjara.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdsjara.algafood.domain.model.FotoProduto;
import com.jdsjara.algafood.domain.repository.ProdutoRepository;

@Service
public class CatalogoFotoProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Transactional
	public FotoProduto salvar(FotoProduto foto) {
		// Se existir uma foto para o produto, o produto será excluído e
		// será cadastrado uma nova foto para o produto. Se caso não existir um
		// produto, será cadastrada a primeira foto para o produto.
		Optional<FotoProduto> fotoExistente = produtoRepository
				.findFotoById(foto.getRestauranteId(), foto.getProduto().getId());
		if (fotoExistente.isPresent()) {
			produtoRepository.delete(fotoExistente.get());
		}
		
		return produtoRepository.save(foto);
	}
	
}
