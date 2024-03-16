package com.jdsjara.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdsjara.algafood.domain.model.Pedido;
import com.jdsjara.algafood.domain.repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
public class FluxoPedidoService {

	@Autowired
	private EmissaoPedidoService emissaoPedido;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Transactional
	public void confirmar(String codigoPedido) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
		pedido.confirmar();
		
		// Para disparar o evento de confirmação de pedido na classe Pedido e no
		// método Confirmar(), precisamos SALVAR o pedido com o SAVE do Repositório
		// Apenas para efetuar o disparo do evento. 
		// Obs: Mesmo não chamando o SAVE, o pedido seria persistido.
		// O intuito (SAVE) é apenas para acionar o evento de confirmação de Pedido.
		pedidoRepository.save(pedido);
	}

	@Transactional
	public void entregar(String codigoPedido) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
		pedido.entregar();
	}

	@Transactional
	public void cancelar(String codigoPedido) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
		pedido.cancelar();
	}
	
}
