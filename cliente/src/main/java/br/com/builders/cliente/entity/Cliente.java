package br.com.builders.cliente.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.builders.cliente.dto.ClienteDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4466393228159026123L;

	@Id
	@Column(name = "id_cliente")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private String cpf;

	private String email;

	@Column(name = "dt_nascimento")
	private LocalDate dataNascimento;
	
	@Transient
	public ClienteDto toDto() {
		return ClienteDto.builder()
				.cpf(this.cpf)
				.dataNascimento(this.dataNascimento)
				.email(this.email)
				.id(this.id)
				.nome(this.nome)
				.build();
	}

}
