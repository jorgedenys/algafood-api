package com.jdsjara.algafood.api.model.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoInput {
	
	@NotNull
	private Long produtoId;
	
	@NotNull
	@Positive
    private Integer quantidade;
	
    private String observacao;
	
}
