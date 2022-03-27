package br.com.builders.cliente.service;

import javax.validation.Valid;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import br.com.builders.cliente.dto.ClienteDto;
import br.com.builders.cliente.entity.Cliente;

public interface ClienteService {

	PageImpl<Cliente> getTodosCliente(Integer page, Integer size, Sort.Direction sortDir, String sort, String field, String search);

	Cliente getClienteById(Long id);

	Long cadastrarCliente(@Valid ClienteDto clienteDto);

	void atualizarTodosCamposDoCliente(ClienteDto clienteDto);

	void atualizarParcialmenteDadosDoCliente(ClienteDto clienteDto);

	Cliente findCliente(Long id);


	
}
