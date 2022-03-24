package br.com.builders.cliente.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.builders.cliente.dto.ClienteDto;
import br.com.builders.cliente.entity.Cliente;
import br.com.builders.cliente.service.ClienteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/cliente", produces = "application/json;charset=UTF-8")
public class ClienteController {
	
	@Autowired
	private ClienteService cliente;

	@GetMapping
	@ApiOperation(value = "Recuperar todos os Clientes", notes = "")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso"),
			@ApiResponse(code = 206, message = "Existe mais registro para paginação"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 500, message = "Erro ao processar sua requisição") })
	public ResponseEntity<List<ClienteDto>>  findAllCliente(
			@ApiParam(value = "Página", required = false, allowEmptyValue = true, example = "0", type = "int", name = "page") 
			@RequestParam(required = false, name = "page", defaultValue = "0") final Integer page,

			@ApiParam(value = "Tamanho da página", required = false, allowEmptyValue = true, example = "100", type = "int", name = "size") 
			@RequestParam(required = false, name = "size", defaultValue = "100") final Integer size,

			@ApiParam(value = "Pesquisar", required = false, allowEmptyValue = true, example = "Luis", type = "string", name = "search")
			@RequestParam(required = false, name = "search") final String search,
			
			@ApiParam(value = "Direção", required = false, allowEmptyValue = true, example = "ASC", type = "string", name = "sortDir")
			@RequestParam(required = false, name = "sortDir", defaultValue = "ASC") Sort.Direction sortDir,
			
			@ApiParam(value = "Campo para ordenacao", required = false, allowEmptyValue = true, example = "nome", type = "string", name = "sort")
			@RequestParam(required = false, name = "ordernar", defaultValue = "nome") String sort) {
		
		PageImpl<Cliente> pageImpl = cliente.getTodosCliente(page, size, sortDir, sort);
		
		HttpStatus httpStatus = HttpStatus.PARTIAL_CONTENT;
		
		if(pageImpl.isLast())
			httpStatus = HttpStatus.OK;
		
		return ResponseEntity.status(httpStatus).body(pageImpl.getContent().stream().map(Cliente::toDto).collect(Collectors.toList())) ;
	}
	
	@GetMapping(value = "/{id}")
	@ApiOperation(value = "Recuperar Cliente por Identificador", notes = "")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 500, message = "Erro ao processar sua requisição") })
	@ResponseBody
	public Cliente getCliente(
			@ApiParam(value = "Identificador", required = true, allowEmptyValue = false, example = "0", type = "int", name = "id") 
			@PathVariable(required = false, name = "id") final Long id) {

		log.info("Deu certo");
		return null;
	}
	
	@PostMapping
	@ApiOperation(value = "Cadastrar Cliente", notes = "")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 500, message = "Erro ao processar sua requisição") })
	@ResponseBody
	public Long cadastrarCliente(
			@Valid
			@ApiParam(value = "Cliente", required = true, allowEmptyValue = false) 
			@RequestBody(required = true) final ClienteDto cliente) {
		
		cliente.getIdade();
		
		log.info("Deu certo");
		return null;
	}
	
	@PutMapping(value = "/{id}")
	@ApiOperation(value = "Atualizar Cliente", notes = "")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 500, message = "Erro ao processar sua requisição") })
	@ResponseStatus(HttpStatus.OK)
	public void atualizarCliente(
			@ApiParam(value = "Identificador", required = true, allowEmptyValue = false, example = "0", type = "int", name = "id") 
			@PathVariable(required = false, name = "id") final Long id,
			
			@ApiParam(value = "Cliente", required = true, allowEmptyValue = false) 
			@RequestBody(required = true) final ClienteDto cliente) {

		log.info("Deu certo");
	}
	
	@PatchMapping(value = "/{id}")
	@ApiOperation(value = "Atualizar Parciamente Cliente", notes = "")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Sucesso"),
			@ApiResponse(code = 403, message = "Acesso negado"),
			@ApiResponse(code = 500, message = "Erro ao processar sua requisição") })
	@ResponseStatus(HttpStatus.OK)
	public void atualizarParcialCliente(
			@ApiParam(value = "Identificador", required = true, allowEmptyValue = false, example = "0", type = "int", name = "id") 
			@PathVariable(required = false, name = "id") final Long id,
			
			@ApiParam(value = "Cliente", required = true, allowEmptyValue = false) 
			@RequestBody(required = true) final ClienteDto cliente) {

		log.info("Deu certo");
	}
}
