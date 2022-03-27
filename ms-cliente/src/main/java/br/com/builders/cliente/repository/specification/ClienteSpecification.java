package br.com.builders.cliente.repository.specification;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.builders.cliente.entity.Cliente;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class ClienteSpecification implements Specification<Cliente>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2995963759032245062L;
	
	private SearchCriteria criteria;

	@Override
	public Predicate toPredicate(Root<Cliente> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		
		if (criteria.getOperation().equalsIgnoreCase(">")) {
			return montaOperacaoMaioIgual(root, builder);
        } 
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
        	return montaOperacaoMenorIgual(root, builder);
        } 
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            return montaOperacaoLikeIgual(root, builder);
        }
		return null;
	}

	private Predicate montaOperacaoLikeIgual(Root<Cliente> root, CriteriaBuilder builder) {
		if (root.get(criteria.getKey()).getJavaType() == String.class) {
		    return builder.like(
		      root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
		} else {
		    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
		}
	}

	private Predicate montaOperacaoMenorIgual(Root<Cliente> root, CriteriaBuilder builder) {
		if(root.get(criteria.getKey()).getJavaType() == LocalDate.class)
			 return builder.lessThanOrEqualTo(root.get(criteria.getKey()), (LocalDate)criteria.getValue());
		else
		    return builder.lessThanOrEqualTo(
		      root.<String> get(criteria.getKey()), criteria.getValue().toString());
	}

	private Predicate montaOperacaoMaioIgual(Root<Cliente> root, CriteriaBuilder builder) {
		if(root.get(criteria.getKey()).getJavaType() == LocalDate.class)
			 return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), (LocalDate)criteria.getValue());
		else
		    return builder.greaterThanOrEqualTo(
		      root.<String> get(criteria.getKey()), criteria.getValue().toString());
	}

}
