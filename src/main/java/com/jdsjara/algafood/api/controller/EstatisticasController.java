package com.jdsjara.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jdsjara.algafood.domain.filter.VendaDiariaFilter;
import com.jdsjara.algafood.domain.model.dto.VendaDiaria;
import com.jdsjara.algafood.domain.service.VendaQueryService;

@RestController
@RequestMapping(value = "/estatisticas")
public class EstatisticasController {
	
	@Autowired
	private VendaQueryService vendaQueryService;
	
	@GetMapping("/vendas-diarias")
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {
		return vendaQueryService.consultarVendasDiarias(filtro);
	}
	
}
