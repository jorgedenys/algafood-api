package com.jdsjara.algafood.domain.repository.filter;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoFilter {
	
	private Long clienteId;
	private Long restauranteId;
	private OffsetDateTime dataCriacaoInicio;
	private OffsetDateTime dataCriacaoFim;
	
}
