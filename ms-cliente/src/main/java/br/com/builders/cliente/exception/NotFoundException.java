package br.com.builders.cliente.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Setter
@Getter
public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String fieldName;
	
	public NotFoundException(String message) {
		super(message);
	}
	
	public NotFoundException(String message, String fieldName) {
		super(message);
		this.fieldName = fieldName;
	}
	
}
