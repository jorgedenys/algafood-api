package com.jdsjara.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.jdsjara.algafood.domain.enums.StatusPedido;
import com.jdsjara.algafood.domain.event.PedidoConfirmadoEvent;
import com.jdsjara.algafood.domain.exception.NegocioException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
public class Pedido extends AbstractAggregateRoot<Pedido> {
	
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String codigo;
	
	@Column(nullable = false)
	private BigDecimal subtotal;
	
	@Column(nullable = false)
	private BigDecimal taxaFrete;
	
	@Column(nullable = false)
	private BigDecimal valorTotal;
	
	@CreationTimestamp
	@Column(nullable = false)
	private OffsetDateTime dataCriacao;

	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataCancelamento;
	private OffsetDateTime dataEntrega;
	
	// Anotação que determina como esse Enum será gravado no banco de dados
	@Enumerated(EnumType.STRING)
	private StatusPedido status = StatusPedido.CRIADO;
	
	@Embedded
	private Endereco enderecoEntrega;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private FormaPagamento formaPagamento;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Restaurante restaurante;
	
	@ManyToOne
	@JoinColumn(name = "usuario_cliente_id", nullable = false)
	private Usuario cliente;
	
	// CascadeType.ALL: Para salvar um Pedido e os itens do pedido em cascata
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private List<ItemPedido> itens = new ArrayList<>();
	
	public void calcularValorTotal() {
	    getItens().forEach(ItemPedido::calcularPrecoTotal);
	    
	    this.subtotal = getItens().stream()
	        .map(item -> item.getPrecoTotal())
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	    
	    this.valorTotal = this.subtotal.add(this.taxaFrete);
	}
	
	public void confirmar() {
		setStatus(StatusPedido.CONFIRMADO);
		setDataConfirmacao(OffsetDateTime.now());
		
		// Método é para registrar um evento que deve ser publicado, disparado quando
		// o objeto pedido for confirmado. Ao confirmar o pedido esse evento será acionado.
		// Deverá ser passado a instância do evento que aconteceu com todas as informações
		// relevantes desse evento.
		// Aqui estamos registrando um evento que deve ser disparado assim que o objeto 
		// dessa entidade Pedido for Salvo no repositório.
		registerEvent(new PedidoConfirmadoEvent(this));
	}
	
	public void entregar() {
		setStatus(StatusPedido.ENTREGUE);
		setDataEntrega(OffsetDateTime.now());
	}
	
	public void cancelar() {
		setStatus(StatusPedido.CANCELADO);
		setDataCancelamento(OffsetDateTime.now());
	}
	
	private void setStatus(StatusPedido novoStatus) {
		if (!getStatus().podeAlterarPara(novoStatus)) {
			throw new NegocioException(
					String.format("Status do pedido %s não pode ser alterado de %s para %s",
							getCodigo(),
							getStatus().getDescricao(), 
							novoStatus.getDescricao()));
		}
		
		this.status = novoStatus;
	}
	
	// MÉTODO DE CALLBACK DO JPA.
	// ANTES DE PERSISTIR PEDIDO, O JPA IRÁ EXECUTAR ESSE MÉTODO.	
	@PrePersist
	private void gerarCodigoUUID() {
		this.setCodigo(UUID.randomUUID().toString());
	}
	
}
