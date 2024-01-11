package com.jdsjara.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdsjara.algafood.domain.exception.PedidoNaoEncontradoException;
import com.jdsjara.algafood.domain.model.Pedido;
import com.jdsjara.algafood.domain.repository.PedidoRepository;

@Service
public class EmissaoPedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	public Pedido buscarOuFalhar(Long pedidoId) {
		return pedidoRepository
				.findById(pedidoId)
				.orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
	}
	
}
