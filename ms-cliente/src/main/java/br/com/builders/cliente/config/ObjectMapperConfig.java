package br.com.builders.cliente.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class ObjectMapperConfig {

	
	@Bean
	public ObjectMapper objectMapper() {
	    return new ObjectMapper()
	            .setDefaultPropertyInclusion(Include.NON_NULL)
	            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
	            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
	            .findAndRegisterModules();
	}
}
