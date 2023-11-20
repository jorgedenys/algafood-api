package com.jdsjara.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.jdsjara.algafood.domain.exception.EntidadeEmUsoException;
import com.jdsjara.algafood.domain.exception.EstadoNaoEncontradoException;
import com.jdsjara.algafood.domain.model.Estado;
import com.jdsjara.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {

	private static final String MSG_ESTADO_EM_USO 
		= "Estado de código %d não pode ser removido, pois está em uso";

	@Autowired
	private EstadoRepository estadoRepository;

	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}
	
	public void excluir(Long estadoId) {
		try {
			Optional<Estado> estado = estadoRepository.findById(estadoId);
			
			if (estado.isEmpty()) {
				throw new EstadoNaoEncontradoException(estadoId);	
			} else {
				estadoRepository.delete(estado.get());	
			}
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_ESTADO_EM_USO, estadoId));
		}
	}

	public Estado buscarOuFalhar(Long estadoId) {
		return estadoRepository.findById(estadoId).orElseThrow(
			() -> new EstadoNaoEncontradoException(estadoId));
	}
	
}
