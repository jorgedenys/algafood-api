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

import com.jdsjara.algafood.api.assembler.GrupoModelAssembler;
import com.jdsjara.algafood.api.model.GrupoModel;
import com.jdsjara.algafood.domain.model.Usuario;
import com.jdsjara.algafood.domain.service.CadastroUsuarioService;

@RestController
@RequestMapping("/usuarios/{usuarioId}/grupos")
public class UsuarioGrupoController {

	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
		
	@Autowired
	private GrupoModelAssembler grupoResumoModelAssembler;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<GrupoModel> listar(@PathVariable Long usuarioId) {
		Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
		
		return grupoResumoModelAssembler.toCollectionModel(usuario.getGrupos());
	}
	
	@PutMapping("{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associarGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		cadastroUsuarioService.associarGrupo(usuarioId, grupoId);
	}
	
	@DeleteMapping("{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociarGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
		cadastroUsuarioService.desassociarGrupo(usuarioId, grupoId);
	}
	
}
