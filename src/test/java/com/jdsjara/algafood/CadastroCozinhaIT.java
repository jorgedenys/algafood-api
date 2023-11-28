package com.jdsjara.algafood;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CadastroCozinhaIT {

	@LocalServerPort
	private int port;
	
	@BeforeEach
	public void setUp() {
		// Habilita o log de qual foi a requisição e qual foi a resposta
		// se caso a validação falhar 
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
		RestAssured.basePath = "/cozinhas";
		RestAssured.port = port;
	}

	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void deveConter4Cozinhas_QuandoConsultarCozinhas() {
		// Validando o corpo da resposta
		// .body("", Matchers.hasSize(4)) - Se retorna 4 itens
		// .body("nome", Matchers.hasItems("Indiana", "Tailandesa")); - Se contém os nomes
		RestAssured.given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(4))
			.body("nome", Matchers.hasItems("Indiana", "Tailandesa"));
	}

}
