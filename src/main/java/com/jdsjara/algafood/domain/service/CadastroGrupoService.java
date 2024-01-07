package com.jdsjara.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdsjara.algafood.domain.exception.EntidadeEmUsoException;
import com.jdsjara.algafood.domain.exception.GrupoNaoEncontradoException;
import com.jdsjara.algafood.domain.model.Grupo;
import com.jdsjara.algafood.domain.model.Permissao;
import com.jdsjara.algafood.domain.repository.GrupoRepository;

@Service
public class CadastroGrupoService {

	private static final String MSG_GRUPO_EM_USO 
	= "Grupo de código %d não pode ser removido, pois está em uso";

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CadastroPermissaoService cadastroPermissao;
	
	@Transactional
	public Grupo salvar(Grupo grupo) {
		grupo = grupoRepository.save(grupo); 
		return grupo;
	}
	
	@Transactional
	public void excluir(Long grupoId) {
		try {
			Optional<Grupo> grupo = grupoRepository.findById(grupoId);
			
			if (grupo.isPresent()) {
				grupoRepository.delete(grupo.get());
			} else {
				throw new GrupoNaoEncontradoException(grupoId);
			}
			
			grupoRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_GRUPO_EM_USO, grupoId));
		}
	}
	
	public Grupo buscarOuFalhar(Long grupoId) {
		return grupoRepository.findById(grupoId).orElseThrow(
			() -> new GrupoNaoEncontradoException(grupoId));
	}

	@Transactional
	public void associarPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = buscarOuFalhar(grupoId);
		Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId); 
		
		grupo.adicionarPermissao(permissao);
	}
	
	@Transactional
	public void desassociarPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = buscarOuFalhar(grupoId);
		Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId); 
		
		grupo.removerPermissao(permissao);
	}
	
}
