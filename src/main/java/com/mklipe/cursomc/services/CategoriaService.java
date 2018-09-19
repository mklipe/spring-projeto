package com.mklipe.cursomc.services;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mklipe.cursomc.domain.Categoria;
import com.mklipe.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		
		return obj.orElse(null);
		
	}
	
	public void salvar() {
		//repo.saveAll(Arrays.asList(cat1, cat2));
	}
	
}
