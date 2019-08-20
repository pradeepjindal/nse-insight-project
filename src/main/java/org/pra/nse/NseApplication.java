package org.pra.nse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class NseApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(NseApplication.class);

	public static void main(String[] args) {
		GreetingInScala greetingInScala = new GreetingInScala();
		SpringApplication.run(NseApplication.class, args);
		LOGGER.info(greetingInScala.greet());

		Processor processor = new Processor();
		processor.process();
	}

}
