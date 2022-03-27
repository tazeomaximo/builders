package br.com.builders.cliente;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = { "br.com.builders.cliente" })
public class ClienteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClienteApplication.class, args);
	}

	
	@PostConstruct
	private void initTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
		log.info("Spring boot application running in UTC timezone :" + new Date());
	}
}
