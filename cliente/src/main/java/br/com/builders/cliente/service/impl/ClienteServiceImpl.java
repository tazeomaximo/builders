package br.com.builders.cliente.service.impl;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.builders.cliente.entity.Cliente;
import br.com.builders.cliente.repository.ClienteRepository;
import br.com.builders.cliente.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService{
	
	private ClienteRepository clienteRepository;
	
	public ClienteServiceImpl(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	@Override
	public PageImpl<Cliente> getTodosCliente(Integer page, Integer size, Sort.Direction sortDir, String sort) {
		
		PageRequest pageReq = PageRequest.of(page, size, sortDir, sort);
		
		return clienteRepository.findAll(pageReq);
	}

}
