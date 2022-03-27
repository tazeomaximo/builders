package br.com.builders.cliente.repository;

import java.util.Optional;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.builders.cliente.entity.Cliente;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente>{

	PageImpl<Cliente> findAll(Pageable pageable);
	
	/**
	 * Verifica se existe algum E-mail cadastrado
	 * 
	 * @param email
	 * @return
	 */
	@Query("select true from #{#entityName} c where c.email = :email")
	Optional<Boolean> existsByEmail(String email);
	
	/**
	 * Verifica se existe algum CPF cadastrado
	 * 
	 * @param cpf
	 * @return
	 */
	@Query("select true from #{#entityName} c where c.cpf = :cpf ")
	Optional<Boolean> existsByCpf(String cpf);
	
	/**
	 *  Verifica se existe algum E-mail cadastrado, mas que não seja o proprio registro
	 *  
	 * @param email
	 * @param id
	 * @return
	 */
	@Query("select true from #{#entityName} c where c.email = :email and c.id != :id")
	Optional<Boolean> existsByEmail(String email, Long id);
	
	/**
	 * Verifica se existe algum CPF cadastrado, mas que não seja o proprio registro
	 * 
	 * @param cpf
	 * @param id
	 * @return
	 */
	@Query("select true from #{#entityName} c where c.cpf = :cpf and c.id != :id")
	Optional<Boolean> existsByCpf(String cpf, Long id);

}
