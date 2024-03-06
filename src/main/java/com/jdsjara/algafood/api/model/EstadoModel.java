package com.jdsjara.algafood.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoModel {
	
	@Schema(example = "1")
	private Long id;
	
	@Schema(example = "Pernambuco")
	private String nome;
	
}
