package com.mklipe.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mklipe.cursomc.domain.Cidade;
import com.mklipe.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	CidadeRepository cidadeRepo;
	
	public List<Cidade> findByEstado(Integer estadoId){
		return cidadeRepo.findCidades(estadoId);
	}
	
}
