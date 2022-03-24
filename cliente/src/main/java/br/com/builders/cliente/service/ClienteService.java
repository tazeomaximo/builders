package br.com.builders.cliente.service;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import br.com.builders.cliente.entity.Cliente;

public interface ClienteService {

	PageImpl<Cliente> getTodosCliente(Integer page, Integer size, Sort.Direction sortDir, String sort);

	
}
