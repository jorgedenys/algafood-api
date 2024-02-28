package com.jdsjara.algafood.domain.service;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdsjara.algafood.domain.model.FotoProduto;
import com.jdsjara.algafood.domain.repository.ProdutoRepository;
import com.jdsjara.algafood.domain.service.FotoStorageService.NovaFoto;

@Service
public class CatalogoFotoProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private FotoStorageService fotoStorageService;
	
	@Transactional
	public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo) {
		String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
		String nomeArquivoExistente = null;
		
		// Se existir uma foto para o produto, o produto será excluído e
		// será cadastrado uma nova foto para o produto. Se caso não existir um
		// produto, será cadastrada a primeira foto para o produto.
		Optional<FotoProduto> fotoExistente = produtoRepository
				.findFotoById(foto.getRestauranteId(), foto.getProduto().getId());
		
		if (fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			produtoRepository.delete(fotoExistente.get());
		}
		
		foto.setNomeArquivo(nomeNovoArquivo);
		foto =  produtoRepository.save(foto);
		// flush() -> Força que o EntityManager do JPA descarregue no banco as 
		// transações queestão na fila do JPA e devam ser realizadas
		produtoRepository.flush();
		
		NovaFoto novaFoto = NovaFoto.builder()
				.nomeArquivo(foto.getNomeArquivo())
				.inputStream(dadosArquivo)
				.build();
				
		fotoStorageService.substituir(nomeArquivoExistente, novaFoto);
		
		return foto;
	}
	
}
