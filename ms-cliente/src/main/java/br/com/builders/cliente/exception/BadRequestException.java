package br.com.builders.cliente.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Setter
@Getter
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String fieldName;
	
	public BadRequestException(String message) {
		super(message);
	}
	
	public BadRequestException(String message, String fieldName) {
		super(message);
		this.fieldName = fieldName;
	}
	
}
