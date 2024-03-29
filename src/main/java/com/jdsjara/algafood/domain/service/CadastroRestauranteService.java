
package com.jdsjara.algafood.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdsjara.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.jdsjara.algafood.domain.model.Cidade;
import com.jdsjara.algafood.domain.model.Cozinha;
import com.jdsjara.algafood.domain.model.FormaPagamento;
import com.jdsjara.algafood.domain.model.Restaurante;
import com.jdsjara.algafood.domain.model.Usuario;
import com.jdsjara.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;
	
	@Autowired
	private CadastroCidadeService cadastroCidade;
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamento;
	
	@Autowired
	private CadastroUsuarioService cadastroUsuario;
	
	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Long cidadeId = restaurante.getEndereco().getCidade().getId();
		
		Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId); 
		
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);
		
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
	
	@Transactional
	public void ativar(List<Long> restauranteIds) {
		// outra forma de implementar
		// restauranteIds.forEach(this::ativar);
		restauranteIds.forEach(restauranteId -> ativar(restauranteId));
	}
	
	@Transactional
	public void inativar(List<Long> restauranteIds) {
		// outra forma de implementar
		// restauranteIds.forEach(restauranteId -> inativar(restauranteId));
		restauranteIds.forEach(this::inativar);
	}
	
	@Transactional
	public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
		
		restaurante.removerFormaPagamento(formaPagamento);
	}
	
	@Transactional
	public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
		
		restaurante.adicionarFormaPagamento(formaPagamento);
	}
	
	public Restaurante buscarOuFalhar(Long restauranteId) {
		return restauranteRepository.findById(restauranteId)
			.orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}

	@Transactional
	public void fechamento(Long restauranteId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		
		restaurante.fechar();
	}
	
	@Transactional
	public void abertura(Long restauranteId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		
		restaurante.abrir();
	}

	@Transactional
	public void associarResponsavel(Long restauranteId, Long responsavelId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		Usuario usuario = cadastroUsuario.buscarOuFalhar(responsavelId);
		
		restaurante.adicionarResponsavel(usuario);
	}

	@Transactional
	public void desassociarResponsavel(Long restauranteId, Long responsavelId) {
		Restaurante restaurante = buscarOuFalhar(restauranteId);
		Usuario usuario = cadastroUsuario.buscarOuFalhar(responsavelId);
		
		restaurante.removerResponsavel(usuario);
	}
	
}
