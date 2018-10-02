package com.mklipe.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.mklipe.cursomc.domain.Cliente;
import com.mklipe.cursomc.domain.Pedido;

public interface EmailService {
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage message);
	
	void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	void sendHtmlEmail(MimeMessage msg);

	void sendNewPasswordEmail(Cliente cliente, String newPass);
	
}
