package com.mklipe.cursomc.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.mklipe.cursomc.services.exceptions.AuthorizationException;
import com.mklipe.cursomc.services.exceptions.DataIntegrityException;
import com.mklipe.cursomc.services.exceptions.FileException;
import com.mklipe.cursomc.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException exception, 
			HttpServletRequest request)
	{
		StandardError erro =
				new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(), 
						"Não encontrado", exception.getMessage(), request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException exception, 
			HttpServletRequest request)
	{
		StandardError erro = 
				new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), 
						"Integridade dos dados", exception.getMessage(), request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException exception, 
			HttpServletRequest request)
	{
		ValidationError erro = 
				new ValidationError(System.currentTimeMillis(), HttpStatus.UNPROCESSABLE_ENTITY.value(), 
						"Erro de validação", exception.getMessage(), request.getRequestURI());
		
		
		for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
			erro.addError(fieldError.getField(), fieldError.getDefaultMessage());
		}
		
		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(erro);
	}

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandardError> authorization(AuthorizationException exception, 
			HttpServletRequest request)
	{
		StandardError erro = 
				new StandardError(System.currentTimeMillis(), HttpStatus.FORBIDDEN.value(), 
						"Acesso negado", exception.getMessage(), request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
	}
	
	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandardError> file(FileException exception, 
			HttpServletRequest request)
	{
		StandardError erro = 
				new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), 
						"Erro de arquivo", exception.getMessage(), request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}
	
	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandardError> amazonService(AmazonServiceException exception, 
			HttpServletRequest request)
	{
		HttpStatus code = HttpStatus.valueOf(exception.getErrorCode());
		
		StandardError erro = 
				new StandardError(System.currentTimeMillis(), code.value(), 
						"Erro Amazon Service", exception.getMessage(), request.getRequestURI());
				
		
		return ResponseEntity.status(code).body(erro);
	}
	
	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandardError> amazonClient(AmazonClientException exception, 
			HttpServletRequest request)
	{
		StandardError erro = 
				new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), 
						"Erro Amazon Client", exception.getMessage(), request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}
	
	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandardError> amazonS3(AmazonS3Exception exception, 
			HttpServletRequest request)
	{
		StandardError erro = 
				new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), 
						"Erro Amazon S3", exception.getMessage(), request.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}
}
