package com.jdsjara.algafood.api.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.jdsjara.algafood.api.assembler.FormaPagamentoInputDisassembler;
import com.jdsjara.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.jdsjara.algafood.api.model.FormaPagamentoModel;
import com.jdsjara.algafood.api.model.input.FormaPagamentoInput;
import com.jdsjara.algafood.domain.model.FormaPagamento;
import com.jdsjara.algafood.domain.repository.FormaPagamentoRepository;
import com.jdsjara.algafood.domain.service.CadastroFormaPagamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {
	
	@Autowired
	private CadastroFormaPagamentoService cadastroFormaPagamento;
	
	@Autowired
	private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	
	@Autowired
	private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;
	
	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<FormaPagamentoModel>> listar(ServletWebRequest request) {
		
		// Implementação para aplicar o Cache - requisições condicionais com Deep ETags
		// Desabilitar o ShallowEtagHeaderFilter para o Deep ETags funcionar
		/*
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		// Se estiver vazio, nenhuma forma de pagamento: a ETag será zero
		String eTag = "0";
		
		OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();
		if (dataUltimaAtualizacao != null) {
			// Retorna o número de segundos desde 1970 até a data de atualização
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		// Fiz a consulta e não mudou nada. Não preciso continuar o processamento.
		if (request.checkNotModified(eTag)) {
			return null;
		}
		*/
		
		List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();
		
		List<FormaPagamentoModel> formasPagamentosModel = formaPagamentoModelAssembler
				.toCollectionModel(todasFormasPagamentos);
		
		// Habilitando o cache com o cabeçalho Cache-Control e a diretiva max-age
		// Pode testar com uma extensão de browser TALEND API TESTER
		return ResponseEntity.ok()
				
				// Default é cachePublic()
				//.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
				
				// Esta diretiva indica que responses seja apenas para usuário único 
				// e não deva ser armazenada em um Shared Cache.
				//.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
				
				// Esta diretiva informa que nenhum cache deverá armazenar o response
				// não importando se é um cache local ou se é um cache compartilhado
				//.cacheControl(CacheControl.noStore())
				
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
				//.eTag(eTag)
				.body(formasPagamentosModel);
	}
	
	@GetMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.OK)
	public FormaPagamentoModel buscar(@PathVariable Long formaPagamentoId) {
		FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
		
		return formaPagamentoModelAssembler.toModel(formaPagamento);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoModel adicionar(
			@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {

		FormaPagamento formaPagamento = 
				formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);
		
		formaPagamento = cadastroFormaPagamento.salvar(formaPagamento);
		
		return formaPagamentoModelAssembler.toModel(formaPagamento);
	}
	
	@PutMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.OK)
	public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId,
			@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
		
		FormaPagamento formaPagamentoAtual = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
		
		formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);
		
		formaPagamentoAtual = cadastroFormaPagamento.salvar(formaPagamentoAtual);
		
		return formaPagamentoModelAssembler.toModel(formaPagamentoAtual);
	}
	
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long formaPagamentoId) {
		cadastroFormaPagamento.excluir(formaPagamentoId);
	}
	
}
