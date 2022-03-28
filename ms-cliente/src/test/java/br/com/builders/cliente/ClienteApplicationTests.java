package br.com.builders.cliente;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.builders.cliente.controller.ControllerTest;
import br.com.builders.cliente.service.ClienteServiceTeste;

@RunWith(Suite.class)
@SuiteClasses(value = {
		ClienteServiceTeste.class,
		ControllerTest.class
})
public class ClienteApplicationTests {

	
}
