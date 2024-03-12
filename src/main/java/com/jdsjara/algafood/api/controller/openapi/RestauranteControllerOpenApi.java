package com.jdsjara.algafood.api.controller.openapi;

import java.util.List;

import com.jdsjara.algafood.api.model.RestauranteModel;
import com.jdsjara.algafood.api.model.input.RestauranteInput;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface RestauranteControllerOpenApi {

	@Operation(parameters = {
		@Parameter(name = "projecao",
				description = "Nome da projeção",
				example = "apenas-nome",
				in = ParameterIn.QUERY,
				required = false
		)
	})
	List<RestauranteModel> listar();

	RestauranteModel buscar(Long restauranteId);

	RestauranteModel adicionar(RestauranteInput restauranteInput);

	RestauranteModel atualizar(Long restauranteId,RestauranteInput restauranteInput);

	void ativar(Long restauranteId);

	void inativar(Long restauranteId);

	void ativarMultiplos(List<Long> restauranteIds);

	void inativarMultiplos(List<Long> restauranteIds);

}
