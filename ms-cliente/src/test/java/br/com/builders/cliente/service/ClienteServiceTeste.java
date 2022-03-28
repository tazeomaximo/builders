package br.com.builders.cliente.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import br.com.builders.cliente.dto.ClienteDto;
import br.com.builders.cliente.entity.Cliente;
import br.com.builders.cliente.exception.BadRequestException;
import br.com.builders.cliente.exception.NotFoundException;
import br.com.builders.cliente.repository.ClienteRepository;
import br.com.builders.cliente.repository.specification.ClienteSpecification;
import br.com.builders.cliente.repository.specification.SearchCriteria;
import br.com.builders.cliente.service.impl.ClienteServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClienteServiceTeste{
	
	
	private static final String IDADE = "idade";

	private static final String DATA_NASCIMENTO = "dataNascimento";

	@Mock
	private ClienteRepository clienteRepository;
	
	@InjectMocks
	private ClienteServiceImpl clienteService;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@Test
	public void getTodosCliente() {
		
		String field = "nome";
		String search = "luiz";
		String sort = field;
		
		PageRequest pageReq = PageRequest.of(0, 100, Sort.Direction.ASC, sort);
		
		Page<Cliente> pageImpl = getPageImpl();
		
		var spec = new ClienteSpecification(new SearchCriteria(field, ":", search));
		
		when(clienteRepository.findAll(spec, pageReq)).thenReturn(pageImpl);
		
		Page<Cliente> pageImplResponse = clienteService.getTodosCliente(0, 100,Sort.Direction.ASC, sort, field, search);
		
		assertEquals(pageImplResponse, pageImpl);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getTodosClienteIdade() {
		
		String field = IDADE;
		String search = "36";
		String sort = field;
		
		Page<Cliente> pageImpl = getPageImpl();
		
		when(clienteRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(pageImpl);
		
		Page<Cliente> pageImplResponse = clienteService.getTodosCliente(0, 100,Sort.Direction.ASC, sort, field, search);
		
		assertEquals(pageImplResponse, pageImpl);
	}
	
	@Test
	public void getTodosClienteDataNascimento() {
		
		String field = DATA_NASCIMENTO;
		String search = "10/04/1987";
		String sort = field;
		
		PageRequest pageReq = PageRequest.of(0, 100, Sort.Direction.ASC, DATA_NASCIMENTO);
		
		Page<Cliente> pageImpl = getPageImpl();
		
		var spec = new ClienteSpecification(new SearchCriteria(DATA_NASCIMENTO, ":",  LocalDate.parse(search, DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		
		when(clienteRepository.findAll(spec, pageReq)).thenReturn(pageImpl);
		
		Page<Cliente> pageImplResponse = clienteService.getTodosCliente(0, 100,Sort.Direction.ASC, sort, field, search);
		
		assertEquals(pageImplResponse, pageImpl);
	}
	
	@Test
	public void getClienteById() {
		Long id = 1L;
		Cliente cliente = Cliente.builder()
				.id(1L)
				.nome("Diego")
				.cpf("85415495002")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(28))
				.build();
		
		Optional<Cliente> optional = Optional.of(cliente);
		
		when(clienteRepository.findById(id)).thenReturn(optional);
		
		Cliente clienteReturn =  clienteService.getClienteById(id);
		
		assertEquals(clienteReturn, cliente);
	}
	
	@Test(expected = NotFoundException.class)
	public void getClienteByIdNotFound() {
		Long id = -1L;
		
		Optional<Cliente> optional = Optional.empty();
		
		when(clienteRepository.findById(id)).thenReturn(optional);
		
		clienteService.getClienteById(id);
		
	}
	
	@Test
	public void cadastrarCliente() {
		Long id = 10L;

		ClienteDto dto = ClienteDto.builder()
				.nome("Diego")
				.cpf("85415495002")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(28))
				.build();
		
		Cliente entity = dto.toEntity();
		entity.setId(id);
		
		when(clienteRepository.save(any(Cliente.class))).thenReturn(entity);
		
		Long idRetorno = clienteService.cadastrarCliente(dto);
		
		assertEquals(id, idRetorno);
	}
	
	@Test(expected = BadRequestException.class)
	public void cadastrarClienteEmail() {
		ClienteDto dto = ClienteDto.builder()
				.cpf("85415495002")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(28))
				.build();
		
		Optional<Boolean> optional = Optional.of(Boolean.TRUE);
		
		when(clienteRepository.existsByEmail(dto.getEmail()))
			.thenReturn(optional);
		
		clienteService.cadastrarCliente(dto);
		
	}
	
	@Test(expected = BadRequestException.class)
	public void cadastrarClienteCpf() {
		ClienteDto dto = ClienteDto.builder()
				.cpf("85415495002")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(28))
				.build();

		Optional<Boolean> optional = Optional.of(Boolean.TRUE);
		
		when(clienteRepository.existsByCpf(dto.getCpf()))
			.thenReturn(optional);
		
		clienteService.cadastrarCliente(dto);
		
	}
	
	@Test
	public void atualizarTodosCamposDoCliente() {
		ClienteDto dto = ClienteDto.builder()
				.id(10L)
				.nome("Diego")
				.cpf("85415495002")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(28))
				.build();
		
		Cliente entity = dto.toEntity();
		
		when(clienteRepository.save(any(Cliente.class))).thenReturn(entity);
		when(clienteRepository.existsById(dto.getId())).thenReturn(true);
		
		clienteService.atualizarTodosCamposDoCliente(dto);
		
		verify(clienteRepository).save(entity);
	}
	
	@Test(expected = NotFoundException.class)
	public void atualizarTodosCamposDoClienteNotFound() {
		ClienteDto dto = ClienteDto.builder()
				.id(10L)
				.nome("Diego")
				.cpf("85415495002")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(28))
				.build();
		
		when(clienteRepository.existsById(dto.getId())).thenReturn(false);
		
		clienteService.atualizarTodosCamposDoCliente(dto);
		
	}
	
	@Test(expected = BadRequestException.class)
	public void atualizarTodosCamposDoClienteEmailExiste() {
		ClienteDto dto = ClienteDto.builder()
				.id(10L)
				.nome("Diego")
				.cpf("85415495002")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(28))
				.build();
		
		Optional<Boolean> optional = Optional.of(Boolean.TRUE);
		
		when(clienteRepository.existsById(dto.getId())).thenReturn(true);
		when(clienteRepository.existsByEmail(dto.getEmail(),dto.getId())).thenReturn(optional);
		
		clienteService.atualizarTodosCamposDoCliente(dto);
		
	}
	
	@Test(expected = BadRequestException.class)
	public void atualizarTodosCamposDoClienteCPFlExiste() {
		ClienteDto dto = ClienteDto.builder()
				.id(10L)
				.nome("Diego")
				.cpf("85415495002")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(28))
				.build();
		
		Optional<Boolean> optional = Optional.of(Boolean.TRUE);
		
		when(clienteRepository.existsById(dto.getId())).thenReturn(true);
		when(clienteRepository.existsByCpf(dto.getCpf(),dto.getId())).thenReturn(optional);
		
		clienteService.atualizarTodosCamposDoCliente(dto);
		
	}
	
	@Test
	public void atualizarParcialmenteDadosDoCliente() throws JsonProcessingException {
		Long id = 2L;
		
		ClienteDto dtoUpdate = ClienteDto.builder()
				.id(id)
				.nome("Diego")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.of(1987,04,18))
				.build();
		
		Cliente clienteDB =Cliente.builder()
				.id(id)
				.nome("Antonio")
				.cpf("85415495002")
				.email("antonio@gmail.com")
				.dataNascimento(LocalDate.of(1958,01,20))
				.build();
		
		
		Optional<Cliente> optionalDB = Optional.of(clienteDB);
		
		String jsonDtoUpdate = " {\"id\":2,\"nome\":\"Diego\",\"email\":\"diego@gmail.com\",\"dataNascimento\":\"18/04/1987\",\"idade\":34}";
		
		Cliente clienteNovo =Cliente.builder()
				.id(id)
				.nome("Diego")
				.cpf("85415495002")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.of(1987,04,18))
				.build();
		
		when(clienteRepository.findById(id)).thenReturn(optionalDB);
		
		when(objectMapper.writeValueAsString(dtoUpdate)).thenReturn(jsonDtoUpdate);
		
		ObjectReader objectReader = mock(ObjectReader.class); 
		
		
		when(objectMapper.readerForUpdating(any())).thenReturn(objectReader);
		
		when(objectMapper.readerForUpdating(any()).readValue(anyString())).thenReturn(clienteNovo.toDto());
		
		clienteService.atualizarParcialmenteDadosDoCliente(dtoUpdate);
		
		verify(clienteRepository).save(clienteNovo);
	}
	
	@Test(expected = ConstraintViolationException.class)
	public void atualizarParcialmenteDadosDoClienteEmailVazio() throws JsonProcessingException {
		Long id = 2L;
		
		ClienteDto dtoUpdate = ClienteDto.builder()
				.id(id)
				.nome("Diego")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.of(1987,04,18))
				.build();
		
		Cliente clienteDB =Cliente.builder()
				.id(id)
				.nome("Antonio")
				.cpf("85415495002")
				.email("antonio@gmail.com")
				.dataNascimento(LocalDate.of(1958,01,20))
				.build();
		
		
		Optional<Cliente> optionalDB = Optional.of(clienteDB);
		
		String jsonDtoUpdate = " {\"id\":2,\"nome\":\"Diego\",\"email\":\"diego@gmail.com\",\"dataNascimento\":\"18/04/1987\",\"idade\":34}";
		
		Cliente clienteNovo =Cliente.builder()
				.id(id)
				.nome("Diego")
				.cpf("85415495002")
				.email(null)
				.dataNascimento(LocalDate.of(1987,04,18))
				.build();
		
		when(clienteRepository.findById(id)).thenReturn(optionalDB);
		
		when(objectMapper.writeValueAsString(dtoUpdate)).thenReturn(jsonDtoUpdate);
		
		ObjectReader objectReader = mock(ObjectReader.class); 
		
		
		when(objectMapper.readerForUpdating(any())).thenReturn(objectReader);
		
		when(objectMapper.readerForUpdating(any()).readValue(anyString())).thenReturn(clienteNovo.toDto());
		
		clienteService.atualizarParcialmenteDadosDoCliente(dtoUpdate);
		
	}
	
	@Test(expected = BadRequestException.class)
	public void atualizarParcialmenteDadosDoClienteEmailExiste() throws JsonProcessingException {
		Long id = 2L;
		
		ClienteDto dtoUpdate = ClienteDto.builder()
				.id(id)
				.nome("Diego")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.of(1987,04,18))
				.build();
		
		Cliente clienteDB =Cliente.builder()
				.id(id)
				.nome("Antonio")
				.cpf("85415495002")
				.email("antonio@gmail.com")
				.dataNascimento(LocalDate.of(1958,01,20))
				.build();
		
		
		Optional<Cliente> optionalDB = Optional.of(clienteDB);
		
		String jsonDtoUpdate = " {\"id\":2,\"nome\":\"Diego\",\"email\":\"diego@gmail.com\",\"dataNascimento\":\"18/04/1987\",\"idade\":34}";
		
		Cliente clienteNovo =Cliente.builder()
				.id(id)
				.nome("Diego")
				.cpf("85415495002")
				.email("diego@gmail.com")
				.dataNascimento(LocalDate.of(1987,04,18))
				.build();
		
		when(clienteRepository.findById(id)).thenReturn(optionalDB);
		
		when(objectMapper.writeValueAsString(dtoUpdate)).thenReturn(jsonDtoUpdate);
		
		ObjectReader objectReader = mock(ObjectReader.class); 
		
		
		when(objectMapper.readerForUpdating(any())).thenReturn(objectReader);
		
		when(objectMapper.readerForUpdating(any()).readValue(anyString())).thenReturn(clienteNovo.toDto());
		
		
		Optional<Boolean> optional = Optional.of(Boolean.TRUE);
		when(clienteRepository.existsByEmail(clienteNovo.getEmail(),clienteNovo.getId())).thenReturn(optional);
		
		clienteService.atualizarParcialmenteDadosDoCliente(dtoUpdate);
		
	}
	
	
	public List<Cliente> getListaCliente(){
		List<Cliente> clientes = new ArrayList<Cliente>(10);
		clientes.add(Cliente.builder()
					.id(1L)
					.nome("Luiz")
					.cpf("34243045810")
					.email("luiz@gmail.com")
					.dataNascimento(LocalDate.now().minusYears(36))
					.build());
		clientes.add(Cliente.builder()
				.id(2L)
				.nome("Taisa")
				.cpf("69455725010")
				.email("taisa@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(30))
				.build());
		clientes.add(Cliente.builder()
				.id(3L)
				.nome("Thiago")
				.cpf("41440455015")
				.email("thiago@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(60))
				.build());
		clientes.add(Cliente.builder()
				.id(4L)
				.nome("Antonio")
				.cpf("33124801083")
				.email("antonio@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(15))
				.build());
		clientes.add(Cliente.builder()
				.id(5L)
				.nome("Natália")
				.cpf("65156059007")
				.email("natalia@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(6))
				.build());
		clientes.add(Cliente.builder()
				.id(6L)
				.nome("João")
				.cpf("70522328040")
				.email("joao@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(19))
				.build());
		clientes.add(Cliente.builder()
				.id(7L)
				.nome("Marcos")
				.cpf("77876630065")
				.email("marcos@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(6))
				.build());
		clientes.add(Cliente.builder()
				.id(8L)
				.nome("Leandro")
				.cpf("48396122008")
				.email("leandro@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(6))
				.build());
		clientes.add(Cliente.builder()
				.id(9L)
				.nome("Rodrigo")
				.cpf("23418929060")
				.email("rodrigo@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(6))
				.build());
		clientes.add(Cliente.builder()
				.id(10L)
				.nome("Fernando")
				.cpf("59197947040")
				.email("fernando@gmail.com")
				.dataNascimento(LocalDate.now().minusYears(6))
				.build());
		
		return clientes;
	}
	
	public PageImpl<Cliente> getPageImpl(){
		return new PageImpl<Cliente>(getListaCliente());
	}

}
