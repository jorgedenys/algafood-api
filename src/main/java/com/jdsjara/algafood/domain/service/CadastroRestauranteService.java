
package com.jdsjara.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdsjara.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.jdsjara.algafood.domain.model.Cozinha;
import com.jdsjara.algafood.domain.model.Restaurante;
import com.jdsjara.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		
		restaurante.setCozinha(cozinha);
		
		return restauranteRepository.save(restaurante);
	}
	
	@Transactional
	public void ativar(Long restauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
		
		// Não precisamos chamar o método save do repositório, pois 
		// quando atualizar a propriedade ATIVO, o objeto que está sendo 
		// gerenciado pelo contexto de persistência do Spring será
		// atualizado automaticamente por ele.
		restauranteAtual.ativar();
	}
	
	@Transactional
	public void inativar(Long restauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
		
		// Não precisamos chamar o método save do repositório, pois 
		// quando atualizar a propriedade ATIVO, o objeto que está sendo 
		// gerenciado pelo contexto de persistência do Spring será
		// atualizado automaticamente por ele.
		restauranteAtual.inativar();
	}
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
		return restauranteRepository.findById(restauranteId)
			.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}
	
}
