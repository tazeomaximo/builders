package br.com.builders.cliente.config;

import java.util.ArrayList;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.builders.cliente.dto.ValidationErroDto;
import br.com.builders.cliente.dto.ViolationDto;
import br.com.builders.cliente.exception.BadRequestException;
import br.com.builders.cliente.exception.NotFoundException;

@ControllerAdvice
public class ErrorHandlingControleAdvice {

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationErroDto onConstraintValidationException(ConstraintViolationException e) {
		ValidationErroDto error = new ValidationErroDto(new ArrayList<ViolationDto>());
		for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
			error.getViolations().add(new ViolationDto(violation.getPropertyPath().toString(), violation.getMessage()));
		}
		return error;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationErroDto onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ValidationErroDto error = new ValidationErroDto(new ArrayList<ViolationDto>());
		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			error.getViolations().add(new ViolationDto(fieldError.getField(), fieldError.getDefaultMessage()));
		}
		return error;
	}
	
	
	@ExceptionHandler(InvalidFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationErroDto onDateTimeParseException(InvalidFormatException e) {
		
		ValidationErroDto error = new ValidationErroDto(new ArrayList<ViolationDto>());
		
		error.getViolations().add(new ViolationDto(e.getPath().get(0).getFieldName(), "Formato inválido"));
		return error;
	}
	
	@ExceptionHandler( BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationErroDto onConstraintViolationException(BadRequestException e) {
		
		ValidationErroDto error = new ValidationErroDto(new ArrayList<ViolationDto>());
		
		error.getViolations().add(new ViolationDto(e.getFieldName(), e.getMessage()));
		return error;
	}
	
	@ExceptionHandler( NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ValidationErroDto onConstraintViolationException(NotFoundException e) {
		
		ValidationErroDto error = new ValidationErroDto(new ArrayList<ViolationDto>());
		
		error.getViolations().add(new ViolationDto(e.getFieldName(), e.getMessage()));
		return error;
	}
	
	@ExceptionHandler( NumberFormatException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationErroDto onConstraintViolationException(NumberFormatException e) {
		
		ValidationErroDto error = new ValidationErroDto(new ArrayList<ViolationDto>());
		
		error.getViolations().add(new ViolationDto("", "Formato inválido"));
		return error;
	}
	
	@ExceptionHandler( ConversionFailedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ValidationErroDto onConversionFailedException(ConversionFailedException e) {
		
		ValidationErroDto error = new ValidationErroDto(new ArrayList<ViolationDto>());
		
		error.getViolations().add(new ViolationDto("", "Formato inválido"));
		return error;
	}

}
