package com.jdsjara.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

import com.jdsjara.algafood.api.ResourceUriHelper;
import com.jdsjara.algafood.api.assembler.CidadeInputDisassembler;
import com.jdsjara.algafood.api.assembler.CidadeModelAssembler;
import com.jdsjara.algafood.api.controller.openapi.CidadeControllerOpenApi;
import com.jdsjara.algafood.api.model.CidadeModel;
import com.jdsjara.algafood.api.model.input.CidadeInput;
import com.jdsjara.algafood.domain.exception.EstadoNaoEncontradoException;
import com.jdsjara.algafood.domain.exception.NegocioException;
import com.jdsjara.algafood.domain.model.Cidade;
import com.jdsjara.algafood.domain.repository.CidadeRepository;
import com.jdsjara.algafood.domain.service.CadastroCidadeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeController implements CidadeControllerOpenApi {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;
	
	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;
	
	@GetMapping
	public CollectionModel<CidadeModel> listar() {
		List<Cidade> cidades = cidadeRepository.findAll();
		
		List<CidadeModel> cidadesModel = cidadeModelAssembler.toCollectionModel(cidades);
		
		cidadesModel.forEach(cidadeModel -> {
			// Comentado devido a problema com o método methodOn
			//cidadeModel.add(WebMvcLinkBuilder.linkTo(methodOn(CidadeController.class).buscar(cidadeModel.getId())).withSelfRel());
		
			//cidadeModel.add(WebMvcLinkBuilder.linkTo(methodOn(CidadeController.class).listar()).withRel("cidades"));
		
			//cidadeModel.getEstado().add(WebMvcLinkBuilder.linkTo(methodOn(EstadoController.class).buscar(cidadeModel.getEstado().getId())).withSelfRel());
		});
		
		CollectionModel<CidadeModel> cidadesCollectionModel = CollectionModel.of(cidadesModel);
		
		cidadesCollectionModel.add(
				WebMvcLinkBuilder.linkTo(CidadeController.class).withSelfRel());
		
		return cidadesCollectionModel;
	}
	
	@GetMapping("/{cidadeId}")
	public CidadeModel buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
		
		CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade); 
		
		// HATEOAS
		// Id da cidade
		// Estático
		//cidadeModel.add(Link.of("http://localhost:8080/cidades/1"));
		//cidadeModel.add(Link.of("http://localhost:8080/cidades/1", IanaLinkRelations.SELF));
		// Construindo links dinâmicos com WebMvcLinkBuilder
		//cidadeModel.add(WebMvcLinkBuilder.linkTo(CidadeController.class).slash(cidadeModel.getId()).withSelfRel());
		
		// Todas as cidades
		//cidadeModel.add(Link.of("http://localhost:8080/cidades", "cidades"));
		//cidadeModel.add(Link.of("http://localhost:8080/cidades", IanaLinkRelations.COLLECTION));
		//cidadeModel.add(WebMvcLinkBuilder.linkTo(CidadeController.class).withRel("cidades"));
		
		// Estado da cidade
		//cidadeModel.getEstado().add(Link.of("http://localhost:8080/estados/1"));
		//cidadeModel.getEstado().add(WebMvcLinkBuilder.linkTo(EstadoController.class).slash(cidadeModel.getEstado().getId()).withSelfRel());
		
		return cidadeModel;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar(@RequestBody CidadeInput cidadeInput) {
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
			
			cidade = cadastroCidade.salvar(cidade);
			
			CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);
			
			ResourceUriHelper.addUriInResponseHeader(cidadeModel.getId());
			
			return cidadeModel;
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@PutMapping("/{cidadeId}")
	public CidadeModel atualizar(@PathVariable Long cidadeId, 
								 @RequestBody @Valid CidadeInput cidadeInput) {
		try {
			Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);
			
			cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			
			cidadeAtual = cadastroCidade.salvar(cidadeAtual);
			
			return cidadeModelAssembler.toModel(cidadeAtual);
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {
		cadastroCidade.excluir(cidadeId);
	}
	
}
