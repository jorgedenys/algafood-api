package com.jdsjara.algafood.domain.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.jdsjara.algafood.domain.event.PedidoConfirmadoEvent;
import com.jdsjara.algafood.domain.model.Pedido;
import com.jdsjara.algafood.domain.service.EnvioEmailService;

@Component
public class NotificacaoClientePedidoConfirmadoListener {

	@Autowired
	private EnvioEmailService envioEmailService;
	
	// Annotation que marca um método como um Listener de eventos
	// Ou seja, um método que está interessado realmente sempre que
	// o evento for lançado. No caso, na confirmação de um Pedido será
	// executado pelo Spring automaticamente este método com a instância
	// do evento que foi disparado.
	@EventListener
	public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
		Pedido pedido = event.getPedido();
		
		// Código para enviar um email.
		// Foi comentado para realização de testes.
		/*
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
				.corpo("pedido-confirmado.html")
				.variavel("pedido", pedido)
				.destinatario(pedido.getCliente().getEmail())
				.build();

		envioEmailService.enviar(mensagem);
		*/
		
		System.out.println("Pedido Confirmado: " + pedido.getRestaurante().getNome());
	}
	
}
