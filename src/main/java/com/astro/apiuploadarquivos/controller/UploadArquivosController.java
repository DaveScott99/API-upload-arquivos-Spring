package com.astro.apiuploadarquivos.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value="/api/upload", produces={"application/json"})
@CrossOrigin("*")
public class UploadArquivosController {
	
	private final String pathArquivos;
	
	public UploadArquivosController(@Value("${app.path.arquivos}") String pathArquivos) {
		this.pathArquivos = pathArquivos;
	}

	@PostMapping("/arquivo")
	public ResponseEntity<String> salvarArquivo(@RequestParam("file") MultipartFile file){
		var caminho = pathArquivos + UUID.randomUUID() + "." + extrairExtensao(file.getOriginalFilename());
		
		try {
			Files.copy(file.getInputStream(), Path.of(caminho), StandardCopyOption.REPLACE_EXISTING);
			return new ResponseEntity<>("{ \"mensagem\": \"Arquivo carregado com sucesso!\"}", HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>("{ \"mensagem\": \"Erro ao carregar o arquivo!\"}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String extrairExtensao(String nomeArquivo) {
		int i = nomeArquivo.lastIndexOf(".");
		return nomeArquivo.substring(i + 1);
	}
	
}
