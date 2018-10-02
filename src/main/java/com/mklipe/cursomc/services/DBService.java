package com.mklipe.cursomc.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mklipe.cursomc.domain.Categoria;
import com.mklipe.cursomc.domain.Cidade;
import com.mklipe.cursomc.domain.Cliente;
import com.mklipe.cursomc.domain.Endereco;
import com.mklipe.cursomc.domain.Estado;
import com.mklipe.cursomc.domain.ItemPedido;
import com.mklipe.cursomc.domain.Pagamento;
import com.mklipe.cursomc.domain.PagamentoComBoleto;
import com.mklipe.cursomc.domain.PagamentoComCartao;
import com.mklipe.cursomc.domain.Pedido;
import com.mklipe.cursomc.domain.Produto;
import com.mklipe.cursomc.domain.enums.EstadoPagamento;
import com.mklipe.cursomc.domain.enums.Perfil;
import com.mklipe.cursomc.domain.enums.TipoCliente;
import com.mklipe.cursomc.repositories.CategoriaRepository;
import com.mklipe.cursomc.repositories.CidadeRepository;
import com.mklipe.cursomc.repositories.ClienteRepository;
import com.mklipe.cursomc.repositories.EnderecoRepository;
import com.mklipe.cursomc.repositories.EstadoRepository;
import com.mklipe.cursomc.repositories.ItemPedidoRepository;
import com.mklipe.cursomc.repositories.PagamentoRepository;
import com.mklipe.cursomc.repositories.PedidoRepository;
import com.mklipe.cursomc.repositories.ProdutoRepository;

@Service
public class DBService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public void instantiateTestDataBase() throws ParseException {
		
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		Categoria cat3 = new Categoria(null, "Cama mesa e banho");
		Categoria cat4 = new Categoria(null, "Eletrônicos");
		Categoria cat5 = new Categoria(null, "Jardinagem");
		Categoria cat6 = new Categoria(null, "Decoração");
		Categoria cat7 = new Categoria(null, "Perfumaria");
		
		Produto p1 = new Produto(null, "Computador", 2000.0);
		Produto p2 = new Produto(null, "Impressora", 800.0);
		Produto p3 = new Produto(null, "Mouse", 80.0);
		Produto p4 = new Produto(null, "Mesa de Escritório", 300.0);
		Produto p5 = new Produto(null, "Toalha", 50.0);
		Produto p6 = new Produto(null, "Colcha", 200.0);
		Produto p7 = new Produto(null, "TV true color", 1200.0);
		Produto p8 = new Produto(null, "Roçadeira", 800.0);
		Produto p9 = new Produto(null, "Abajour", 100.0);
		Produto p10 = new Produto(null, "Pendente", 180.0);
		Produto p11 = new Produto(null, "Shampoo", 90.0);
		
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2, p4));
		cat3.getProdutos().addAll(Arrays.asList(p5, p6));
		cat4.getProdutos().addAll(Arrays.asList(p1, p2, p3, p7));
		cat5.getProdutos().addAll(Arrays.asList(p8));
		cat6.getProdutos().addAll(Arrays.asList(p9, p10));
		cat7.getProdutos().addAll(Arrays.asList(p11));
		
		p1.getCategorias().addAll(Arrays.asList(cat1, cat4));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2, cat4));
		p3.getCategorias().addAll(Arrays.asList(cat1, cat4));
		p4.getCategorias().addAll(Arrays.asList(cat2));
		p5.getCategorias().addAll(Arrays.asList(cat3));
		p6.getCategorias().addAll(Arrays.asList(cat3));
		p7.getCategorias().addAll(Arrays.asList(cat4));
		p8.getCategorias().addAll(Arrays.asList(cat5));
		p9.getCategorias().addAll(Arrays.asList(cat6));
		p10.getCategorias().addAll(Arrays.asList(cat6));
		p11.getCategorias().addAll(Arrays.asList(cat7));	
		
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11));
		
		Estado e1 = new Estado(null, "São Paulo");
		Estado e2 = new Estado(null, "Minas Gerais");
		
		Cidade c1 = new Cidade(null, "Uberlândia", e2);
		Cidade c2 = new Cidade(null, "Campinas", e1);
		Cidade c3 = new Cidade(null, "São Paulo", e1);
		
		e1.getCidades().addAll(Arrays.asList(c2, c3));
		e2.getCidades().addAll(Arrays.asList(c1));
		
		estadoRepository.saveAll(Arrays.asList(e1, e2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		Cliente cli1 = 
				new Cliente(null, "Maria Silva", "maria@gmail.com", "08821721145", TipoCliente.PESSOAFISICA, passwordEncoder.encode("123"));

		cli1.getTelefones().add("98389393");
		cli1.getTelefones().add("27363323");

		Cliente cli2 = 
				new Cliente(null, "Ana Silv Costa", "ana@gmail.com", "07138937966", TipoCliente.PESSOAFISICA, passwordEncoder.encode("123"));

		cli2.addPerfil(Perfil.ADMIN);
		
		cli2.getTelefones().add("981360533");
		cli2.getTelefones().add("30552787");
		
		Endereco end1 = new Endereco(null, "Rua Flores", "300", "Apto 203", "Jardim", "38220834", cli1, c1);
		Endereco end2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "382777012", cli1, c2);
		Endereco end3 = new Endereco(null, "Avenida Floriano", "10", "Sala 8", "Centro", "382777012", cli2, c2);
		
		cli1.getEnderecos().add(end1);
		cli1.getEnderecos().add(end2);
		cli2.getEnderecos().add(end3);

		clienteRepository.saveAll(Arrays.asList(cli1, cli2));
		
		enderecoRepository.saveAll(Arrays.asList(end1, end2, end3));
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, end1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, end2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		
		ped2.setPagamento(pagto2);
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
		
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.0, 1, 2000.0);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.0, 2, 80.0);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.0, 1, 800.0);
		
		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		p1.getItens().add(ip1);
		p2.getItens().add(ip3);
		p3.getItens().add(ip2);
		
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
	}
}
