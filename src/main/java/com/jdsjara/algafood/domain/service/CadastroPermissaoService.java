package com.jdsjara.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jdsjara.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.jdsjara.algafood.domain.model.Permissao;
import com.jdsjara.algafood.domain.repository.PermissaoRepository;

@Service
public class CadastroPermissaoService {

	@Autowired
	private PermissaoRepository permissaoRepository;
	
	public Permissao buscarOuFalhar(Long permissaoId) {
		return permissaoRepository.findById(permissaoId)
				.orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
	}
	
}
