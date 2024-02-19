package com.jdsjara.algafood.api.controller;

import java.util.List;

import com.jdsjara.algafood.api.model.CidadeModel;
import com.jdsjara.algafood.api.model.input.CidadeInput;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cidades", description = "Gerencia as cidades")
public interface CidadeControllerOpenApi {
	
	List<CidadeModel> listar();
	
	CidadeModel buscar(Long cidadeId);

	CidadeModel adicionar(CidadeInput cidadeInput);

	CidadeModel atualizar(Long cidadeId, CidadeInput cidadeInput);
	
	void remover(Long cidadeId);
	
}
