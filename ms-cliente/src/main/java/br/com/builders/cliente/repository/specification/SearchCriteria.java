package br.com.builders.cliente.repository.specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class SearchCriteria {

	
	private String key;
    private String operation;
    private Object value;
}
