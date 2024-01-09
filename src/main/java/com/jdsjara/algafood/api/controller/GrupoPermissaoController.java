package com.jdsjara.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jdsjara.algafood.api.assembler.PermissaoModelAssembler;
import com.jdsjara.algafood.api.model.PermissaoModel;
import com.jdsjara.algafood.domain.model.Grupo;
import com.jdsjara.algafood.domain.service.CadastroGrupoService;

@RestController
@RequestMapping("/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {

	@Autowired
	private CadastroGrupoService cadastroGrupo; 
	
	@Autowired
	private PermissaoModelAssembler permissaoModelAssembler;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<PermissaoModel> listar(@PathVariable Long grupoId) {
		Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);
		
		return permissaoModelAssembler.toCollectionModel(grupo.getPermissoes());
	}
	
	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associarPermissao(@PathVariable Long grupoId, 
			@PathVariable Long permissaoId) {
		cadastroGrupo.associarPermissao(grupoId, permissaoId);
	}
	
	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociarPermissao(@PathVariable Long grupoId, 
			@PathVariable Long permissaoId) {
		cadastroGrupo.desassociarPermissao(grupoId, permissaoId);
	}
	
}
