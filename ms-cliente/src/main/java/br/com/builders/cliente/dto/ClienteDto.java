package br.com.builders.cliente.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.builders.cliente.entity.Cliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("Cliente")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(value = {"id", "nome", "cpf", "email", "data_nascimento"})
public class ClienteDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5738279529500149138L;

	private Long id;
	
	@NotBlank(message = "O nome é obrigatório")
	@Size(min = 3, max=250, message = "O nome deve ter no mínimo {min} e no máximo {max}")
	@ApiModelProperty(name = "nome", example = "Luiz", value = "O nome deve ter no mínimo 3 e no máximo 250")
	private String nome;
	
	@NotBlank(message = "O cpf é obrigatório")
	private String cpf;
	
	@Email(message = "O e-mail não é válido",regexp = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
	@NotBlank(message = "O email é obrigatório")
	@ApiModelProperty(name = "email", example = "gutodarbem@gmail.com")
	private String email;
	
	@NotNull(message = "Data de nascimento é obrigatória")
	@Past(message = "Data de nascimento deve ser uma data passada")
	@ApiModelProperty(name = "data_nascimento", example = "16/12/1987")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate dataNascimento;
	
	@JsonProperty("idade")
	public Long getIdade() {
		if(Objects.nonNull(getDataNascimento()))
			return ChronoUnit.YEARS.between(dataNascimento, LocalDate.now());
		return null;
	}
	
	@JsonIgnore
	public String getCpfSemFormatar() {
		return this.cpf == null ? null : this.cpf.replaceAll("\\D", "");
	}
	
	@JsonIgnore
	public Cliente toEntity() {
		return Cliente.builder()
				.cpf(getCpfSemFormatar())
				.dataNascimento(this.dataNascimento)
				.email(this.email)
				.id(this.id)
				.nome(this.nome)
				.build();
	}
	
}
