package com.jdsjara.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdsjara.algafood.domain.model.Pedido;

import jakarta.transaction.Transactional;

@Service
public class FluxoPedidoService {

	@Autowired
	private EmissaoPedidoService emissaoPedido;
	
	@Transactional
	public void confirmar(Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);
		pedido.confirmar();
	}

	@Transactional
	public void entregar(Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);
		pedido.entregar();
	}

	@Transactional
	public void cancelar(Long pedidoId) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);
		pedido.cancelar();
	}
	
}
