package com.mklipe.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mklipe.cursomc.domain.Estado;
import com.mklipe.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService {

	@Autowired
	EstadoRepository estadoRepo;
	
	public List<Estado> findAll(){
		return estadoRepo.findAllByOrderByNome();
	}
	 
}
