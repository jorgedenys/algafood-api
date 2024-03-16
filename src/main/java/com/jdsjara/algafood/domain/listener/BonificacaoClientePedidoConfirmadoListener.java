package com.jdsjara.algafood.domain.listener;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.jdsjara.algafood.domain.event.PedidoConfirmadoEvent;
import com.jdsjara.algafood.domain.model.Pedido;

@Component
public class BonificacaoClientePedidoConfirmadoListener {

	// Annotation que marca um método como um Listener de eventos
	// Ou seja, um método que está interessado realmente sempre que
	// o evento for lançado. No caso, na confirmação de um Pedido será
	// executado pelo Spring automaticamente este método com a instância
	// do evento que foi disparado.
	// Especifica que qual que a fase específica da transação que o método
	// deve ser disparado. Por padrão, os eventos serão disparados depois da
	// transação efetuar o Commit.
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
		Pedido pedido = event.getPedido();
		System.out.println("Foi gerado pontos de bonificação pelo restaurante "
				+ pedido.getRestaurante().getNome() 
				+ " para o(a) cliente " + pedido.getCliente().getNome());
	}
	
}
