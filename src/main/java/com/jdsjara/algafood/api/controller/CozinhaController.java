package com.jdsjara.algafood.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jdsjara.algafood.api.assembler.CozinhaInputDisassembler;
import com.jdsjara.algafood.api.assembler.CozinhaModelAssembler;
import com.jdsjara.algafood.api.model.CozinhaModel;
import com.jdsjara.algafood.api.model.input.CozinhaInput;
import com.jdsjara.algafood.domain.model.Cozinha;
import com.jdsjara.algafood.domain.repository.CozinhaRepository;
import com.jdsjara.algafood.domain.service.CadastroCozinhaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/cozinhas")
public class CozinhaController {

	private static final Logger logger = LoggerFactory.getLogger(CozinhaController.class);
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CozinhaInputDisassembler cozinhaInputDisassembler;
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;
	
	@GetMapping
	public Page<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable) {
		logger.info("Consultando cozinhas paginadas.");
		
		Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);
		
		List<CozinhaModel> cozinhasModel = cozinhaModelAssembler
				.toCollectionModel(cozinhasPage.getContent());
		
		Page<CozinhaModel> cozinhasModelPage = new PageImpl<>(cozinhasModel,
				pageable, cozinhasPage.getTotalElements());
		
		return cozinhasModelPage;
	}
	
	@GetMapping("/{cozinhaId}")
	public CozinhaModel buscar(@PathVariable Long cozinhaId) {
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		
		return cozinhaModelAssembler.toModel(cozinha);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput); 
		
		cozinha = cadastroCozinha.salvar(cozinha); 
		
		return cozinhaModelAssembler.toModel(cozinha);
	}
	
	@PutMapping("/{cozinhaId}")
	public CozinhaModel atualizar(@PathVariable Long cozinhaId,
								  @RequestBody @Valid CozinhaInput cozinhaInput) {
		Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);
		
		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		
		cozinhaAtual = cadastroCozinha.salvar(cozinhaAtual); 
		
		return cozinhaModelAssembler.toModel(cozinhaAtual);
	}
	
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cadastroCozinha.excluir(cozinhaId);
	}
	
}
