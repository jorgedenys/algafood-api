package com.jdsjara.algafood.domain.service;

import com.jdsjara.algafood.domain.filter.VendaDiariaFilter;

public interface VendaReportService {
	
	byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset);	
	
}
