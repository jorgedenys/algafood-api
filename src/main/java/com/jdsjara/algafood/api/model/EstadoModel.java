package com.jdsjara.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadoModel extends RepresentationModel<EstadoModel> {
	
	@Schema(example = "1")
	private Long id;
	
	@Schema(example = "Pernambuco")
	private String nome;
	
}
