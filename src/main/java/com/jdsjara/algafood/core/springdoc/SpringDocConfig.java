package com.jdsjara.algafood.core.springdoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
/*
@SecurityScheme(name = "security_auth",
				type = SecuritySchemeType.OAUTH2,
				flows = @OAuthFlows(authorizationCode = @OAuthFlow(
							authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
							tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
							scopes = {
									@OAuthScope(name = "READ", description = "read scope"),
									@OAuthScope(name = "WRITE", description = "write scope")
								}
						)))
*/						
public class SpringDocConfig {
	
	@Bean
	OpenAPI openApi() {
		return new OpenAPI()
				.info(new Info()
						.title("Algafood API")
						.version("v")
						.description("REST API do Algafood")
						.license(new License()
								.name("Apache 2.0")
								.url("http://springdoc.com")
						)
				)
				.externalDocs(new ExternalDocumentation()
						.description("Documentação Externa: Página Inicial (SpringDoc)")
						.url("http://localhost:8080/swagger-ui/index.html")
				);
	}
	
}
