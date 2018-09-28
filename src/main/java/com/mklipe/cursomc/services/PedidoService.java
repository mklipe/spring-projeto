package com.mklipe.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mklipe.cursomc.domain.ItemPedido;
import com.mklipe.cursomc.domain.PagamentoComBoleto;
import com.mklipe.cursomc.domain.Pedido;
import com.mklipe.cursomc.domain.enums.EstadoPagamento;
import com.mklipe.cursomc.repositories.ItemPedidoRepository;
import com.mklipe.cursomc.repositories.PagamentoRepository;
import com.mklipe.cursomc.repositories.PedidoRepository;
import com.mklipe.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired 
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
		
	}

	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto,obj.getInstante());
		}
		
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setDesconto(0.0);
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		
		System.out.println(obj);
		
		return obj;
	} 
}
