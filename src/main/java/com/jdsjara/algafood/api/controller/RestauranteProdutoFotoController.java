package com.jdsjara.algafood.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jdsjara.algafood.api.assembler.FotoProdutoModelAssembler;
import com.jdsjara.algafood.api.model.FotoProdutoModel;
import com.jdsjara.algafood.api.model.input.FotoProdutoInput;
import com.jdsjara.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.jdsjara.algafood.domain.model.FotoProduto;
import com.jdsjara.algafood.domain.model.Produto;
import com.jdsjara.algafood.domain.service.CadastroProdutoService;
import com.jdsjara.algafood.domain.service.CatalogoFotoProdutoService;
import com.jdsjara.algafood.domain.service.FotoStorageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {
	
	@Autowired
	private CadastroProdutoService cadastroProduto;
	
	@Autowired
	private CatalogoFotoProdutoService catalogoFotoProduto;
	
	@Autowired
	private FotoProdutoModelAssembler fotoProdutoModelAssembler;
	
	@Autowired
	private FotoStorageService fotoStorageService;
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId,
			@PathVariable Long produtoId, @Valid FotoProdutoInput fotoProdutoInput) throws IOException {
		
		Produto produto = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);
		
		MultipartFile arquivo = fotoProdutoInput.getArquivo();
		
		FotoProduto foto = new FotoProduto();
		foto.setProduto(produto);
		foto.setDescricao(fotoProdutoInput.getDescricao());
		foto.setContentType(arquivo.getContentType());
		foto.setTamanho(arquivo.getSize());
		foto.setNomeArquivo(arquivo.getOriginalFilename());
		
		FotoProduto fotoSalva = catalogoFotoProduto.salvar(foto, arquivo.getInputStream());
		
		return fotoProdutoModelAssembler.toModel(fotoSalva);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public FotoProdutoModel buscar(@PathVariable Long restauranteId, 
			@PathVariable Long produtoId) {
	    FotoProduto fotoProduto = catalogoFotoProduto.
	    		buscarOuFalhar(restauranteId, produtoId);
	    
	    return fotoProdutoModelAssembler.toModel(fotoProduto);
	}
	
	@GetMapping
	public ResponseEntity<InputStreamResource> servirFoto(@PathVariable Long restauranteId, 
			@PathVariable Long produtoId, @RequestHeader(name = "Accept") String acceptHeader) 
					throws HttpMediaTypeNotAcceptableException {
	    try {
	    	FotoProduto fotoProduto = catalogoFotoProduto.buscarOuFalhar(restauranteId, produtoId);
		    
		    // Faz um Parse de uma String (ContentType) em um MediaType
	    	MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
	    	
	    	// Consumidor da API irá passar no accept os tipos aceitáveis(jpg, png...)
	    	// por esse motivo criamos uma List de MediaType
	    	List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);
	    	
	    	// Verifica se o consumidor da API aceita o tipo de arquivo que será baixado
	    	// Caso não aceite, será lançada uma exception
	    	verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);
	    	
	    	InputStream inputStream = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());
		    
		    return ResponseEntity.ok()
		    		.contentType(mediaTypeFoto)
		    		.body(new InputStreamResource(inputStream));	
	    } catch (EntidadeNaoEncontradaException e) {
	    	return ResponseEntity.notFound().build();
	    }
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		catalogoFotoProduto.excluir(restauranteId, produtoId);
	}
	
	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, 
			List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
		
		boolean compativel = mediaTypesAceitas.stream()
				.anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
		
		if (!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
		}
	}
	
}
