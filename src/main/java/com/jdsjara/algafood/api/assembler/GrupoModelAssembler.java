package com.jdsjara.algafood.api.assembler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jdsjara.algafood.api.model.GrupoModel;
import com.jdsjara.algafood.domain.model.Grupo;

@Component
public class GrupoModelAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public GrupoModel toModel(Grupo grupo) {
		return modelMapper.map(grupo, GrupoModel.class);
	}
	
	public Set<GrupoModel> toCollectionModel(Set<Grupo> grupos) {
		return grupos.stream()
				.map(grupo -> toModel(grupo))
				.collect(Collectors.toSet());
	}
	
	public List<GrupoModel> toCollectionModel(List<Grupo> grupos) {
		return grupos.stream()
				.map(grupo -> toModel(grupo))
				.collect(Collectors.toList());
	}
	
}
