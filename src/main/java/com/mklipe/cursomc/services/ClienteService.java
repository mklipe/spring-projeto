package com.mklipe.cursomc.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mklipe.cursomc.domain.Cidade;
import com.mklipe.cursomc.domain.Cliente;
import com.mklipe.cursomc.domain.Endereco;
import com.mklipe.cursomc.domain.enums.TipoCliente;
import com.mklipe.cursomc.dto.ClienteDTO;
import com.mklipe.cursomc.dto.ClienteNewDTO;
import com.mklipe.cursomc.repositories.ClienteRepository;
import com.mklipe.cursomc.repositories.EnderecoRepository;
import com.mklipe.cursomc.services.exceptions.DataIntegrityException;
import com.mklipe.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public Cliente find(Integer id) {
		UserSS user = UserService.authenticated();

		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())){
			throw new AuthorizationException("Acesso negado!");
		}

		Optional<Cliente> obj = clienteRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));	
	} 

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = clienteRepository.save(obj);
		
		enderecoRepository.saveAll(obj.getEnderecos());
		
		return obj;
	}

	public Cliente update(Cliente obj) {
		
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		
		return clienteRepository.save(newObj);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public void delete(Integer id) {
		find(id);
		
		try {	
			clienteRepository.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma Cliente com pedidos relacionados.");
		}
	}

	public List<ClienteDTO> findAll() {
		List<Cliente> lista = clienteRepository.findAll();
		List<ClienteDTO> listaDTO = 
				lista.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
			
		return listaDTO;
	} 
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
		
	}
	
	public Cliente fromDTO(ClienteDTO objDTO){
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null);
	}

	public Cliente fromDTO(@Valid ClienteNewDTO objDTO) {
		Cliente cli = 
				new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()), passwordEncoder.encode(objDTO.getSenha()) );
		
		Cidade cid = new Cidade(objDTO.getCidadeId(), null, null);
		
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), 
				objDTO.getBairro(), objDTO.getCep(), cli, cid);
		
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		
		if (objDTO.getTelefone2() != null) {
			cli.getTelefones().add(objDTO.getTelefone2());
		}
		
		if (objDTO.getTelefone3() != null) {
			cli.getTelefones().add(objDTO.getTelefone3());
		}
		
		return cli;
		
	}
	
}
