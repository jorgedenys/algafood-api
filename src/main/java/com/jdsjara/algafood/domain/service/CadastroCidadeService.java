package com.jdsjara.algafood.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdsjara.algafood.domain.exception.CidadeNaoEncontradaException;
import com.jdsjara.algafood.domain.exception.EntidadeEmUsoException;
import com.jdsjara.algafood.domain.model.Cidade;
import com.jdsjara.algafood.domain.model.Estado;
import com.jdsjara.algafood.domain.repository.CidadeRepository;

@Service
public class CadastroCidadeService {

	private static final String MSG_CIDADE_EM_USO 
		= "Cidade de código %d não pode ser removida, pois está em uso";

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;

	@Transactional
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();

		Estado estado = cadastroEstado.buscarOuFalhar(estadoId);

		cidade.setEstado(estado);

		return cidadeRepository.save(cidade);
	}

	@Transactional
	public void excluir(Long cidadeId) {
		try {
			Optional<Cidade> cidade = cidadeRepository.findById(cidadeId);
			
			if (cidade.isEmpty()) {
				throw new CidadeNaoEncontradaException(cidadeId);	
			} else {
				cidadeRepository.delete(cidade.get());	
			}
			
			cidadeRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, cidadeId));
		}
	}

	public Cidade buscarOuFalhar(Long cidadeId) {
		return cidadeRepository.findById(cidadeId).orElseThrow(
			() -> new CidadeNaoEncontradaException(cidadeId));
	}

}
