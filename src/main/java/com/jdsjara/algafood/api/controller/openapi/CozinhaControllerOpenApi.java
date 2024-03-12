package com.jdsjara.algafood.api.controller.openapi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jdsjara.algafood.api.controller.openapi.annotation.PageableParameter;
import com.jdsjara.algafood.api.model.CozinhaModel;
import com.jdsjara.algafood.api.model.input.CozinhaInput;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cozinhas")
public interface CozinhaControllerOpenApi {

	@PageableParameter
	Page<CozinhaModel> listar(@Parameter(hidden = true) Pageable pageable);

	CozinhaModel buscar(Long cozinhaId);

	CozinhaModel adicionar(CozinhaInput cozinhaInput);

	CozinhaModel atualizar(Long cozinhaId, CozinhaInput cozinhaInput);

	void remover(Long cozinhaId);

}
