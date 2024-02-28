package com.jdsjara.algafood.infrastructure.service.storage;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.jdsjara.algafood.domain.service.FotoStorageService;

@Service
public class LocalFotoStorageService implements FotoStorageService {

	@Value("${algafood.storage.local.diretorio-fotos}")
	private Path diretorioFotos;
	
	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			// Caminho onde será armazenado o arquivo da foto
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());
			
			// Copia os dados do arquivo recebido para 
			// o caminho que está informado no Path
			FileCopyUtils.copy(novaFoto.getInputStream(), 
					Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			throw new StorageException("Não foi possível armazenar arquivo.", e);
		}
	}
	
	@Override
	public void remover(String nomeArquivo) {
		try {
			// Caminho onde está armazenado o arquivo da foto
			Path arquivoPath = getArquivoPath(nomeArquivo);

			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possível excluir arquivo.", e);
		}
	}
	
	private Path getArquivoPath(String nomeArquivo) {
		return diretorioFotos.resolve(Path.of(nomeArquivo));
	}

}
