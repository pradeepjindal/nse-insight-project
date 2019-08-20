package org.pra.nse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class NseApplication {
	private static Logger LOGGER = LoggerFactory.getLogger(NseApplication.class);

	public static void main(String[] args) {
		GreetingInScala greetingInScala = new GreetingInScala();
		SpringApplication.run(NseApplication.class, args);
		LOGGER.info(greetingInScala.greet());

//		Processor1 processor1 = new Processor1();
//		processor1.process();

//		Processor2 processor2 = new Processor2();
//		processor2.process();

		Processor3 processor3 = new Processor3();
		processor3.process();
	}

}
