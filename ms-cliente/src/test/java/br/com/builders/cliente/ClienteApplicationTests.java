package br.com.builders.cliente;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.com.builders.cliente.dto.ClienteDto;
import br.com.builders.cliente.entity.Cliente;
import br.com.builders.cliente.repository.ClienteRepository;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = ClienteApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteApplicationTests {

	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClienteRepository clienteRepository;
	
	@LocalServerPort
	private int port;
	
	private String url;

	public String getUrl() {
		if (url == null) {
			url = "http://localhost:" + port + "/";
		}
		return url;
	}
	
	private List<Cliente> getClientes(){
		List<Cliente> clientes = new ArrayList<Cliente>(10);
		clientes.add(Cliente.builder()
					.id(18L)
					.nome("Antonio Silva")
					.cpf("52006706080")
					.email("antoniosilva@gmail.com")
					.dataNascimento(LocalDate.of(1986,7,25))
					.build());
		
		clientes.add(Cliente.builder()
				.id(9L)
				.nome("Celso Silva")
				.cpf("58303413015")
				.email("celso@gmail.com")
				.dataNascimento(LocalDate.of(1991,3,5))
				.build());
		
		return clientes;
	}
	
	public PageImpl<Cliente> getPageImpl(){
		return new PageImpl<Cliente>(getClientes());
	}
	
	@Test
	public void listarClientes() throws Exception {
		
		when(clienteRepository.findAll(any(PageRequest.class))).thenReturn(getPageImpl());
		
		mockMvc.perform(get(getUrl().concat("cliente")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].email", is("antoniosilva@gmail.com")));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void listarClientesFiltro() throws Exception {
		
		when(clienteRepository.findAll(any(Specification.class),any(PageRequest.class))).thenReturn(getPageImpl());
		
		mockMvc.perform(get(getUrl().concat("cliente?field=nome&search=Silva")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].email", is("antoniosilva@gmail.com")));
	}

	
	@Test
	public void cadastrarCliente() throws Exception {
		Long id = 1L;
		
		ClienteDto dto = ClienteDto.builder()
				.nome("Mauri Augusto")
				.cpf("53222852022")
				.email("mauri@gmail.com")
				.dataNascimento(LocalDate.of(2000,3,14))
				.build();
		
		Cliente cliente = dto.toEntity();
		cliente.setId(id);
		
		
		String json = "{"
				+ "    \"nome\": \"Mauri Augusto\","
				+ "    \"cpf\": \"53222852022\","
				+ "    \"email\": \"mauri@gmail.com\","
				+ "    \"dataNascimento\": \"14/03/2000\""
				+ "}";
		
		Optional<Boolean> optional = Optional.empty();
		
		when(clienteRepository.save(dto.toEntity())).thenReturn(cliente);
		
		when(clienteRepository.existsByCpf(dto.getCpfSemFormatar())).thenReturn(optional);
		
		when(clienteRepository.existsByEmail(dto.getEmail())).thenReturn(optional);
		
		mockMvc.perform(post(getUrl().concat("cliente"))
						.content(json)
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated());
		
		verify(clienteRepository).save(dto.toEntity());
	}
	
	@Test
	public void cadastrarClienteEmailVazio() throws Exception {
		String json = "{"
				+ "    \"nome\": \"Mauri Augusto\","
				+ "    \"cpf\": \"53222852022\","
				+ "    \"dataNascimento\": \"14/03/2000\""
				+ "}";
		
		mockMvc.perform(post(getUrl().concat("cliente"))
						.content(json)
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.violations[0].fieldName", is("email")));
	}
	
	@Test
	public void cadastrarClienteCpfVazio() throws Exception {
		String json = "{"
				+ "    \"nome\": \"Mauri Augusto\","
				+ "    \"email\": \"mauri@gmail.com\","
				+ "    \"dataNascimento\": \"14/03/2000\""
				+ "}";
		
		mockMvc.perform(post(getUrl().concat("cliente"))
				.content(json)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.violations[0].fieldName", is("cpf")));
	}
	
}
