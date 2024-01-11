package com.jdsjara.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jdsjara.algafood.api.assembler.PedidoModelAssembler;
import com.jdsjara.algafood.api.model.PedidoModel;
import com.jdsjara.algafood.domain.model.Pedido;
import com.jdsjara.algafood.domain.repository.PedidoRepository;
import com.jdsjara.algafood.domain.service.EmissaoPedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	private EmissaoPedidoService emissaoPedido;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<PedidoModel> listar() {
		List<Pedido> pedidos = pedidoRepository.findAll();
		
		return pedidoModelAssembler.toCollectionModel(pedidos);
	}
	
	@GetMapping("{pedidoId}")
	@ResponseStatus(HttpStatus.OK)
	public PedidoModel buscar(@PathVariable Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);
		
		return pedidoModelAssembler.toModel(pedido);
	}
	
}
