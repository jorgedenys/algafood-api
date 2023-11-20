package com.jdsjara.algafood;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.jdsjara.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.jdsjara.algafood.domain.exception.EntidadeEmUsoException;
import com.jdsjara.algafood.domain.model.Cozinha;
import com.jdsjara.algafood.domain.service.CadastroCozinhaService;

import jakarta.validation.ConstraintViolationException;

@SpringBootTest
public class CadastroCozinhaIntegrationTests {

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Test
	public void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {
		
		// CENÁRIO
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		
		// AÇÃO
		novaCozinha = cadastroCozinha.salvar(novaCozinha);
		
		// VALIDAÇÃO
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test
	public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
		
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);
		
		ConstraintViolationException erroEsperado =
				Assertions.assertThrows(ConstraintViolationException.class,
						() -> {cadastroCozinha.salvar(novaCozinha);});
		
		assertThat(erroEsperado).isNotNull();
		
	}
	
	@Test
	public void deveFalhar_QuandoExcluirCozinhaEmUso() {

		EntidadeEmUsoException erroEsperado = 
				Assertions.assertThrows(EntidadeEmUsoException.class,
						() -> {cadastroCozinha.excluir(1L);
		});

		assertThat(erroEsperado).isNotNull();

	}

	@Test
	public void deveFalhar_QuandoExcluirCozinhaInexistente() {

		CozinhaNaoEncontradaException erroEsperado = 
				Assertions.assertThrows(CozinhaNaoEncontradaException.class, 
						() -> {cadastroCozinha.excluir(80L);});

		assertThat(erroEsperado).isNotNull();

	}

}