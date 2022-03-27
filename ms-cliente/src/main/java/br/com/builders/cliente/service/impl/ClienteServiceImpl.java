package br.com.builders.cliente.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

	private static final String O_CAMPO_DE_PESQUISA_NAO_EXISTE = "O campo de pesquisa não existe";
	private static final String OPERATION_EQUAL = ":";
	private static final String DATA_NASCIMENTO = "dataNascimento";
	private static final String IDADE = "idade";
	private static final String DD_MM_YYYY = "dd/MM/yyyy";
	private static final String DATA_DE_NASCIMENTO_DEVE_SER_VALIDA = "Data de nascimento deve ser válida";
	private static final String IDADE_DEVE_SER_UM_NUMERO = "Idade deve ser um número";
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

		if (IDADE.equalsIgnoreCase(sort)) {
			sort = DATA_NASCIMENTO;
		}
		
		validarExistenciaDeCampo(sort);
		
		PageRequest pageReq = PageRequest.of(page, size, sortDir, sort);
		
		if(Objects.isNull(field)) {
			return clienteRepository.findAll(pageReq);
		}
		
		if(Objects.isNull(search))
			throw new BadRequestException("Search é obrigatório", "search");
		
		Specification<Cliente> spec = null;
		
		if (IDADE.equalsIgnoreCase(field)) {
			field = DATA_NASCIMENTO;
			spec = montaFiltroIdade(field, search);
		}else if(DATA_NASCIMENTO.equalsIgnoreCase(field)){
			spec = montaFiltroDtNasc(field, search);
		}else {
			spec = new ClienteSpecification(new SearchCriteria(field, OPERATION_EQUAL, search));
		}

		validarExistenciaDeCampo(field); 

		Page<Cliente> pageCliente = clienteRepository.findAll(spec, pageReq);

		if(pageCliente.getContent().isEmpty() && pageCliente.getTotalElements() == 0)
			throw clienteNaoEncontradoException(field);
		
		if(pageCliente.getTotalPages() <= page)
			throw new BadRequestException("Página maior que o total ("+pageCliente.getTotalPages()+").", "page"); 
		
		return (PageImpl<Cliente>) pageCliente;
	}

	private Specification<Cliente> montaFiltroDtNasc(String field, String search) {
		Specification<Cliente> spec = null;
		try {
			LocalDate dateSearch = LocalDate.parse(search, DateTimeFormatter.ofPattern(DD_MM_YYYY));
			spec = new ClienteSpecification(new SearchCriteria(field, OPERATION_EQUAL, dateSearch));
		}catch(DateTimeParseException e) {
			log.error("Erro ao formatar dataNascimento: {}", search);
			throw new BadRequestException(DATA_DE_NASCIMENTO_DEVE_SER_VALIDA, DATA_NASCIMENTO);
		}
		return spec;
	}

	private Specification<Cliente> montaFiltroIdade(String field, String search) {
		Specification<Cliente> specReturn = null;
		try {
			LocalDate dateSearch = LocalDate.now().minusYears(Integer.parseInt(search));
			LocalDate initDateSearch = LocalDate.of(dateSearch.getYear(), 1, 1);
			LocalDate endDateSearch = LocalDate.of(dateSearch.getYear(), 12, 31);
			
			ClienteSpecification spec = new ClienteSpecification(new SearchCriteria(field, "<", endDateSearch));
			specReturn = Specification.where(spec).and(new ClienteSpecification(new SearchCriteria(field, ">", initDateSearch)));
		}catch(NumberFormatException e) {
			log.error("Erro ao formatar ano da idade: {}", search);
			throw new BadRequestException(IDADE_DEVE_SER_UM_NUMERO, IDADE);
		}
		return specReturn;
	}
	
	private void validarExistenciaDeCampo(String field) {
		try {
			Cliente.class.getDeclaredField(field);
		} catch (NoSuchFieldException e) {
			log.error("Campo {} nao existe", field);
			throw new BadRequestException(O_CAMPO_DE_PESQUISA_NAO_EXISTE, field);
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
	public Long cadastrarCliente(ClienteDto clienteDto) {

		if (clienteRepository.existsByEmail(clienteDto.getEmail()).isPresent())
			throw emailJaExisteException();

		if (clienteRepository.existsByCpf(clienteDto.getCpfSemFormatar()).isPresent())
			throw cpfJaExisteException();

		Cliente cliente = clienteRepository.save(clienteDto.toEntity());

		return cliente.getId();
	}

	@Override
	public void atualizarTodosCamposDoCliente(ClienteDto clienteDto) {
		validarClienteExistente(clienteDto);

		if (clienteRepository.existsByEmail(clienteDto.getEmail(), clienteDto.getId()).isPresent())
			throw emailJaExisteException();

		if (clienteRepository.existsByCpf(clienteDto.getCpfSemFormatar(), clienteDto.getId()).isPresent())
			throw cpfJaExisteException();


		clienteRepository.save(clienteDto.toEntity());

	}

	private void validarClienteExistente(ClienteDto clienteDto) {
		if (!clienteRepository.existsById(clienteDto.getId()))
			throw clienteNaoEncontradoException(ID);
	}

	@Override
	public void atualizarParcialmenteDadosDoCliente(ClienteDto clienteDto) {
		Cliente cliente = clienteRepository.findById(clienteDto.getId())
				.orElseThrow(() -> clienteNaoEncontradoException(ID));

		ClienteDto clienteUpdated = atualizarAtributos(clienteDto, cliente.toDto());

		validarDados(clienteUpdated);

		validarValoresJaExistentes(clienteDto);

		clienteRepository.save(clienteUpdated.toEntity());
	}

	private void validarValoresJaExistentes(ClienteDto clienteDto) {
		if (Objects.nonNull(clienteDto.getEmail())
				&& clienteRepository.existsByEmail(clienteDto.getEmail(), clienteDto.getId()).isPresent())
			throw emailJaExisteException();

		if (Objects.nonNull(clienteDto.getCpf())
				&& clienteRepository.existsByCpf(clienteDto.getCpfSemFormatar(), clienteDto.getId()).isPresent())
			throw cpfJaExisteException();
	}

	private ClienteDto atualizarAtributos(ClienteDto clienteDto, ClienteDto clienteDtoBD) {
		ClienteDto clienteUpdated = null;
		try {
			String json = objectMapper.writeValueAsString(clienteDto);

			clienteUpdated = objectMapper.readerForUpdating(clienteDtoBD).readValue(json);

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
