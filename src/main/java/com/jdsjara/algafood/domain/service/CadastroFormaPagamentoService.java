package com.jdsjara.algafood.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.jdsjara.algafood.domain.exception.CidadeNaoEncontradaException;
import com.jdsjara.algafood.domain.exception.EntidadeEmUsoException;
import com.jdsjara.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.jdsjara.algafood.domain.model.Cidade;
import com.jdsjara.algafood.domain.model.FormaPagamento;
import com.jdsjara.algafood.domain.repository.FormaPagamentoRepository;

import jakarta.transaction.Transactional;

@Service
public class CadastroFormaPagamentoService {
	
	private static final String MSG_FORMA_PAGAMENTO_EM_USO 
		= "Forma de pagamento de código %d não pode ser removida, pois está em uso";
	
	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return formaPagamentoRepository.save(formaPagamento);
	}
	
	@Transactional
	public void excluir(Long formaPagamentoId) {
		try {
			Optional<FormaPagamento> formaPagamento = formaPagamentoRepository.findById(formaPagamentoId);
			
			if (formaPagamento.isEmpty()) {
				throw new FormaPagamentoNaoEncontradaException(formaPagamentoId);	
			} else {
				formaPagamentoRepository.delete(formaPagamento.get());	
			}
			
			formaPagamentoRepository.flush();
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_FORMA_PAGAMENTO_EM_USO, formaPagamentoId));
        }
	}
	
	public FormaPagamento buscarOuFalhar(Long formaPagamentoId) {
        return formaPagamentoRepository.findById(formaPagamentoId)
            .orElseThrow(() -> new FormaPagamentoNaoEncontradaException(formaPagamentoId));
    }
	
	public List<FormaPagamento> listar() {
		return formaPagamentoRepository.findAll();
	}
	
}
