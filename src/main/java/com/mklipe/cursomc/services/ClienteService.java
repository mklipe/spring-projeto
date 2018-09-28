package com.mklipe.cursomc.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mklipe.cursomc.domain.Cliente;
import com.mklipe.cursomc.dto.ClienteDTO;
import com.mklipe.cursomc.repositories.ClienteRepository;
import com.mklipe.cursomc.services.exceptions.DataIntegrityException;
import com.mklipe.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
		
	} 

	public Cliente insert(Cliente obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public Cliente update(Cliente obj) {
		
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		
		
		return repo.save(newObj);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public void delete(Integer id) {
		find(id);
		
		try {	
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma Cliente porque há entidades relacionadas.");
		}
	}

	public List<ClienteDTO> findAll() {
		List<Cliente> lista = repo.findAll();
		List<ClienteDTO> listaDTO = 
				lista.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
			
		return listaDTO;
	} 
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
		
	}
	
	public Cliente fromDTO(ClienteDTO objDTO){
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail());
	}
	
}
