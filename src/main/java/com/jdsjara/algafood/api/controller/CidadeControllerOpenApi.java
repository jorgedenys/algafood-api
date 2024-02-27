package com.jdsjara.algafood.api.controller;

import java.util.List;

import com.jdsjara.algafood.api.model.CidadeModel;
import com.jdsjara.algafood.api.model.input.CidadeInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cidades", description = "Gerencia as cidades")
public interface CidadeControllerOpenApi {
	
	@Operation(summary = "Listar as cidades")
	List<CidadeModel> listar();
	
	@Operation(summary = "Buscar uma cidade por Id")
	CidadeModel buscar(@Parameter(description = "Id de uma cidade", 
		example = "1", required = true) Long cidadeId);

	@Operation(summary = "Cadastrar uma cidade", description = "Cadastro de uma cidade necessita "
			+ "de um estado e um nome válido")
	CidadeModel adicionar(@RequestBody(description = "Representação de uma nova cidade", 
		required = true) CidadeInput cidadeInput);

	@Operation(summary = "Atualizar uma cidade")
	CidadeModel atualizar(@Parameter(description = "Id de uma cidade", example = "1",
		required = true) Long cidadeId,
			@RequestBody(description = "Representação de uma nova cidade", 
		required = true) CidadeInput cidadeInput);
	
	@Operation(summary = "Excluir uma cidade")
	void remover(@Parameter(description = "Id de uma cidade", example = "1",
		required = true) Long cidadeId);
	
}
