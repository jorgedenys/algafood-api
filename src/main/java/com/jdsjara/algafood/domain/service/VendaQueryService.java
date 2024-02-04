package com.jdsjara.algafood.domain.service;

import java.util.List;

import com.jdsjara.algafood.domain.filter.VendaDiariaFilter;
import com.jdsjara.algafood.domain.model.dto.VendaDiaria;

public interface VendaQueryService {
	
	List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro);	
	
}
