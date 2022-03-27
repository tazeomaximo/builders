package br.com.builders.cliente.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErroDto {

	
	private List<ViolationDto> violations ;
}
