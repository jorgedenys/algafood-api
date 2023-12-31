package com.jdsjara.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdsjara.algafood.domain.exception.NegocioException;
import com.jdsjara.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.jdsjara.algafood.domain.model.Usuario;
import com.jdsjara.algafood.domain.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		Usuario usuario = buscarOuFalhar(usuarioId);
		
		if (!usuario.senhaCoincideCom(senhaAtual)) {
			throw new NegocioException("Senha atual informada não coincide "
					+ "com a senha do usuário.");
		}
		
		usuario.setSenha(novaSenha);
		//usuarioRepository.save(usuario);
	}
	
	public Usuario buscarOuFalhar(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}

}
