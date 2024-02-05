package com.jdsjara.algafood.infrastructure.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jdsjara.algafood.domain.enums.StatusPedido;
import com.jdsjara.algafood.domain.filter.VendaDiariaFilter;
import com.jdsjara.algafood.domain.model.Pedido;
import com.jdsjara.algafood.domain.model.dto.VendaDiaria;
import com.jdsjara.algafood.domain.service.VendaQueryService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {
	
	@PersistenceContext
	private EntityManager entityManager;
		
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, 
			String timeOffset) {
		
		// Obtem uma instância de CriteriaBuilder - utilizado para obter query,
		// predicates, função de agregação
		var builder = entityManager.getCriteriaBuilder();
		
		// Criação de uma Lista de Predicates (predicados) para os filtros
		var predicates = new ArrayList<Predicate>();
		
		// CriteriaQuery - é o tipo que o método retorna. cria um CriteriaBuilder
		// que retorna um tipo especificado. Quando executar a consulta, 
		// o que espera de retorno dessaconsulta será VendaDiaria.
		var query = builder.createQuery(VendaDiaria.class);
		
		// Define a entidade que será usada na cláusula FROM
		var root = query.from(Pedido.class);
		
		// FUNCTION - Vai criar uma expressão para executar uma função no banco de dados.
		// Ou seja, irá executar a função CONVERT_TZ (Timezone) do banco de dados.
		var functionConvertTzDataCriacao = builder.function(
				"convert_tz",
				LocalDate.class,
				root.get("dataCriacao"),
				builder.literal("+00:00"),
				builder.literal(timeOffset));
		
		// FUNCTION - Vai criar uma expressão para executar uma função no banco de dados.
		// Ou seja, irá executar a função DATE do banco de dados
		var functionDateDataCriacao = builder.function(
				"date", // Função que será executada no BD - date
				LocalDate.class, // Define o tipo do resultado esperado por essa função
				root.get("dataCriacao") // Argumento da função date - Própria data de criação
				//functionConvertTzDataCriacao // Argumento da função date - Timezone
				);
		
		// Define as colunas que serão retornadas na consulta.
		// Construirá VendaDiaria a partir da selection
		var selection = builder.construct(VendaDiaria.class, 
				functionDateDataCriacao, // Função para a data
				builder.count(root.get("id")), // COUNT no ID do Pedido
				builder.sum(root.get("valorTotal")) // SUM no valorTotal do Pedido
				);
		
		// Executa o select de acordo com a projeção (selection) específica informada
		query.select(selection);
		
		// Filtros da consulta
		if (filtro.getRestauranteId() != null) {
			predicates.add(builder.equal(
					root.get("restaurante").get("id"), filtro.getRestauranteId()));
		}

		if(filtro.getDataCriacaoInicio() != null) {
			predicates.add(builder.greaterThanOrEqualTo(
					root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
		}
		
		if(filtro.getDataCriacaoFim() != null) {
			predicates.add(builder.lessThanOrEqualTo(
					root.get("dataCriacao"), filtro.getDataCriacaoFim()));
		}
		
		// Consulta deverá trazer apenas pedidos com Status CONFIRMADO E ENTREGUE
		// Para essa condição foi utilizado o CriteriaBuilder - IN Expression
		predicates.add(root.get("status").in(StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));
				
		// Adicionamos os predicates na consulta
		query.where(predicates.toArray(new Predicate[0]));
		
		// Agrupa a consulta por Data
		query.groupBy(functionDateDataCriacao);
		
		// getResultList() - retorna uma lista de VendaDiaria.
		// Especificado acima através do builder.createQuery(VendaDiaria.class);
		return entityManager.createQuery(query).getResultList();
	}
	
	// Outra forma de se criar. Desse modo o SQL ficou bem visível na classe.
	// Mas é mais suscetível a ter de erros de sintaxe,
	// já que estamos criando o nosso SQL com Strings
	/*
	private static final String SQL_BASE_VENDA_DIARIA = String.join(
			" ",
			"select",
			"date(convert_tz(pedido.data_criacao, :timeOffsetBegin, :timeOffsetEnd)) as data,",
			"count(pedido.id) as totalVendas,",
			"sum(pedido.valor_total) as totalFaturado",
			"from",
			"pedido",
			"where",
			"pedido.status in (:status) "
	);

	private static final String SQL_WHERE_RESTAURANTE = " and pedido.restaurante_id = :restauranteId";

	private static final String SQL_GROUP_BY = " group by date(convert_tz(pedido.data_criacao, :timeOffsetBegin, :timeOffsetEnd)) ";

	private static final String TIME_OFFSET_BEGIN = "+00:00";

	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {

		String sql = gerarSQLDeAcordoComFiltros(filtro);

		Query nativeQuery = manager.createNativeQuery(sql);

		//Parâmetros de acordo com o filtro
		if (filtro.getRestauranteId() != null) {
			nativeQuery.setParameter("restauranteId", filtro.getRestauranteId());
		}

		var status = Arrays.asList(StatusPedido.ENTREGUE.name(), StatusPedido.CONFIRMADO.name());
		//Parâmetros padrão
		nativeQuery.setParameter("status", status);
		nativeQuery.setParameter("timeOffsetBegin", TIME_OFFSET_BEGIN);
		nativeQuery.setParameter("timeOffsetEnd", timeOffset);

		return (List<VendaDiaria>) nativeQuery.getResultList();
	}

	private String gerarSQLDeAcordoComFiltros(VendaDiariaFilter filtro) {
		var sql = SQL_BASE_VENDA_DIARIA;

		if (filtro.getRestauranteId() != null) {
			sql = sql + SQL_WHERE_RESTAURANTE;
		}

		sql = sql + SQL_GROUP_BY;
		return sql;
	}
	*/
	
	// Outra implementação de exemplo usando JPQL
	// que já vai se aproximar bem do SQL nativo
	/*
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {
		StringBuilder jpql = new StringBuilder(
				"SELECT new com.algaworks.algafood.domain.model.dto.VendaDiaria(" +
				"FUNCTION('date', p.dataCriacao), COUNT(p.id), SUM(p.valorTotal)) " +
				"FROM Pedido p ");

		String where = "";
		boolean setDataCriacaoInicio = false;
		boolean setDataCriacaoFim = false;
		boolean setRestauranteId = false;

		if (filtro.getDataCriacaoInicio() != null) {
			where += "p.dataCriacao >= :dataCriacaoInicio ";
			setDataCriacaoInicio = true;
		}

		if (filtro.getDataCriacaoFim() != null) {
			if (setDataCriacaoInicio) {
				where += "AND ";
			}

			where += "p.dataCriacao <= :dataCriacaoFim ";
			setDataCriacaoFim = true;
		}

		if (filtro.getRestauranteId() != null) {
			if (setDataCriacaoInicio || setDataCriacaoFim) {
				where += "AND ";
			}

			where += "p.restaurante.id = :restauranteId ";
			setRestauranteId = true;
		}

		if (!where.isBlank()) {
			jpql.append("WHERE ").append(where);
		}

		jpql.append("GROUP BY FUNCTION('date', p.dataCriacao)");

		TypedQuery<VendaDiaria> query = manager.createQuery(jpql.toString(), VendaDiaria.class);

		if (setDataCriacaoInicio) {
			query.setParameter("dataCriacaoInicio", filtro.getDataCriacaoInicio());
		}

		if (setDataCriacaoFim) {
			query.setParameter("dataCriacaoFim", filtro.getDataCriacaoFim());
		}

		if (setRestauranteId) {
			query.setParameter("restauranteId", filtro.getRestauranteId());
		}
		
		return query.getResultList();
	}
	*/
	
}
