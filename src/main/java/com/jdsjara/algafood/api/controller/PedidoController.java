package com.jdsjara.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jdsjara.algafood.api.assembler.PedidoInputDisassembler;
import com.jdsjara.algafood.api.assembler.PedidoModelAssembler;
import com.jdsjara.algafood.api.assembler.PedidoResumoModelAssembler;
import com.jdsjara.algafood.api.model.PedidoModel;
import com.jdsjara.algafood.api.model.PedidoResumoModel;
import com.jdsjara.algafood.api.model.input.PedidoInput;
import com.jdsjara.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.jdsjara.algafood.domain.exception.NegocioException;
import com.jdsjara.algafood.domain.model.Pedido;
import com.jdsjara.algafood.domain.model.Usuario;
import com.jdsjara.algafood.domain.repository.PedidoRepository;
import com.jdsjara.algafood.domain.service.EmissaoPedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	private EmissaoPedidoService emissaoPedido;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PedidoModelAssembler pedidoModelAssembler;
	
	@Autowired
	private PedidoResumoModelAssembler pedidoResumoModelAssembler;
	
	@Autowired
	private PedidoInputDisassembler pedidoInputDisassembler;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<PedidoResumoModel> listar() {
		List<Pedido> pedidos = pedidoRepository.findAll();
		
		return pedidoResumoModelAssembler.toCollectionModel(pedidos);
	}
	
	@GetMapping("{codigoPedido}")
	@ResponseStatus(HttpStatus.OK)
	public PedidoModel buscar(@PathVariable String codigoPedido) {
		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
		
		return pedidoModelAssembler.toModel(pedido);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
		try {
			Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);
			
	        // TODO pegar usuário autenticado
	        novoPedido.setCliente(new Usuario());
	        novoPedido.getCliente().setId(1L);

	        novoPedido = emissaoPedido.emitir(novoPedido);

	        return pedidoModelAssembler.toModel(novoPedido);
	    } catch (EntidadeNaoEncontradaException e) {
	    	throw new NegocioException(e.getMessage(), e);
	    }
	}
	
}
