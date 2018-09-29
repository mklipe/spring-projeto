package com.mklipe.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.mklipe.cursomc.domain.Pedido;

public interface EmailService {
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage message);
	
}
