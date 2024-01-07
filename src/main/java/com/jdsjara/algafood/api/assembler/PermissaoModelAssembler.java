package com.jdsjara.algafood.api.assembler;

import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jdsjara.algafood.api.model.PermissaoModel;
import com.jdsjara.algafood.domain.model.Permissao;

@Component
public class PermissaoModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public PermissaoModel toModel(Permissao permissao) {
		return modelMapper.map(permissao, PermissaoModel.class);		
	}
	
	public Set<PermissaoModel> toCollectionModel(Set<Permissao> permissoes) {
		return permissoes.stream()
				.map(permissao -> toModel(permissao))
				.collect(Collectors.toSet());
	}
	
}
