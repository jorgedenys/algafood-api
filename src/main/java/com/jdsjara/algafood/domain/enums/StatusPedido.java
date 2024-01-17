package com.jdsjara.algafood.domain.enums;

import java.util.Arrays;
import java.util.List;

public enum StatusPedido {
	
	CRIADO("Criado"),
	CONFIRMADO("Confirmado", CRIADO),
	ENTREGUE("Entregue", CONFIRMADO),
	CANCELADO("Cancelado", CRIADO);
	
	private String descricao;
	private List<StatusPedido> statusAnteriores;
	
	// Aqui é passado o varArgs de StatusPedido - porque pode ser passado
	// como parâmetro nenhum, um ou vários parâmetros.
	StatusPedido(String descricao, StatusPedido... statusAnteriores) {
		this.descricao = descricao;
		this.statusAnteriores = Arrays.asList(statusAnteriores);
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public boolean podeAlterarPara(StatusPedido novoStatus) {
		return novoStatus.statusAnteriores.contains(this);
	}
	
}
