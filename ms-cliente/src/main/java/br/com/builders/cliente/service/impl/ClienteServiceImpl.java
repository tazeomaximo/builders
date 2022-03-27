package br.com.builders.cliente.service.impl;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.builders.cliente.dto.ClienteDto;
import br.com.builders.cliente.entity.Cliente;
import br.com.builders.cliente.exception.BadRequestException;
import br.com.builders.cliente.exception.NotFoundException;
import br.com.builders.cliente.repository.ClienteRepository;
import br.com.builders.cliente.repository.specification.ClienteSpecification;
import br.com.builders.cliente.repository.specification.SearchCriteria;
import br.com.builders.cliente.service.ClienteService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {

	private static final String ID = "id";
	private static final String EMAIL = "email";
	private static final String CPF = "cpf";
	private static final String NENHUM_CLIENTE_ENCONTRADO = "Nenhum cliente encontrado.";
	private static final String CPF_JA_CADASTRADO = "CPF já cadastrado.";
	private static final String EMAIL_JA_CADASTRADO = "Email já cadastrado.";

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private BadRequestException cpfJaExisteException() {
		return new BadRequestException(CPF_JA_CADASTRADO, CPF);
	}

	private BadRequestException emailJaExisteException() {
		return new BadRequestException(EMAIL_JA_CADASTRADO, EMAIL);
	}

	private NotFoundException clienteNaoEncontradoException(String field) {
		return new NotFoundException(NENHUM_CLIENTE_ENCONTRADO, field);
	}

	@Override
	public PageImpl<Cliente> getTodosCliente(Integer page, Integer size, Sort.Direction sortDir, String sort,
			String field, String search) {

		if ("idade".equalsIgnoreCase(sort)) {
			sort = "dataNascimento";
		}
			
		validarExistenciaDeCampo(sort);
		
		PageRequest pageReq = PageRequest.of(page, size, sortDir, sort);
		
		if(Objects.isNull(field)) {
			return clienteRepository.findAll(pageReq);
		}

		validarExistenciaDeCampo(field); 
		
		if(Objects.isNull(search))
			throw new BadRequestException("Search é obrigatório", "search");
		
		ClienteSpecification spec = new ClienteSpecification(new SearchCriteria(field, ":", search));

		Page<Cliente> pageCliente = clienteRepository.findAll(spec, pageReq);

		if(pageCliente.getContent().isEmpty())
			throw clienteNaoEncontradoException(field);
		
		return (PageImpl<Cliente>) pageCliente;
	}

	private void validarExistenciaDeCampo(String field) {
		try {
			Cliente.class.getDeclaredField(field);
		} catch (NoSuchFieldException e) {
			log.error("Campo {} nao existe", field);
			throw new BadRequestException("O campo de pesquisa não existe", field);
		} catch (SecurityException e) {
			log.error("Erro: {}", e);
			throw e;
		}
	}

	@Override
	public Cliente getClienteById(Long id) {
		return clienteRepository.findById(id).orElseThrow(() -> clienteNaoEncontradoException(ID));
	}

	@Override
	public Long cadastrarCliente(@Valid ClienteDto clienteDto) {

		if (clienteRepository.existsByEmail(clienteDto.getEmail()).isPresent())
			throw emailJaExisteException();

		if (clienteRepository.existsByCpf(clienteDto.getCpfSemFormatar()).isPresent())
			throw cpfJaExisteException();

		Cliente cliente = clienteRepository.save(clienteDto.toEntity());

		return cliente.getId();
	}

	@Override
	public void atualizarTodosCamposDoCliente(ClienteDto clienteDto) {
		if (clienteRepository.existsByEmail(clienteDto.getEmail(), clienteDto.getId()).isPresent())
			throw emailJaExisteException();

		if (clienteRepository.existsByCpf(clienteDto.getCpfSemFormatar(), clienteDto.getId()).isPresent())
			throw cpfJaExisteException();

		validarClienteExistente(clienteDto);

		clienteRepository.save(clienteDto.toEntity());

	}

	private void validarClienteExistente(ClienteDto clienteDto) {
		if (!clienteRepository.existsById(clienteDto.getId()))
			throw clienteNaoEncontradoException(ID);
	}

	@Override
	public Cliente findCliente(Long id) {
		Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> clienteNaoEncontradoException(ID));

		return cliente;

	}

	@Override
	public void atualizarParcialmenteDadosDoCliente(ClienteDto clienteDto) {
		Cliente cliente = clienteRepository.findById(clienteDto.getId())
				.orElseThrow(() -> clienteNaoEncontradoException(ID));

		Cliente clienteUpdated = atualizarAtributos(clienteDto, cliente);

		validarDados(clienteUpdated.toDto());

		validarValoresJaExistentes(clienteDto);

		clienteRepository.save(clienteUpdated);
	}

	private void validarValoresJaExistentes(ClienteDto clienteDto) {
		if (Objects.nonNull(clienteDto.getEmail())
				&& clienteRepository.existsByEmail(clienteDto.getEmail(), clienteDto.getId()).isPresent())
			throw emailJaExisteException();

		if (Objects.nonNull(clienteDto.getCpf())
				&& clienteRepository.existsByCpf(clienteDto.getCpfSemFormatar(), clienteDto.getId()).isPresent())
			throw cpfJaExisteException();
	}

	private Cliente atualizarAtributos(ClienteDto clienteDto, Cliente cliente) {
		Cliente clienteUpdated = null;
		try {
			String json = objectMapper.writeValueAsString(clienteDto);

			clienteUpdated = objectMapper.readerForUpdating(cliente).readValue(json);

		} catch (IOException e) {
			log.error("Erro ao tentar atualizar cliente. {}", e);
		}
		return clienteUpdated;
	}

	private void validarDados(ClienteDto clienteDto) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<ClienteDto>> violations = validator.validate(clienteDto);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

}
