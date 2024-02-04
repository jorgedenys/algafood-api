package com.jdsjara.algafood.domain.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class VendaDiaria {
	
	private LocalDate data;
	private Long totalVendas;
	private BigDecimal totalFaturado;
	
}
