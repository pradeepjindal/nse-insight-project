package org.pra.nse2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Nse2Application {
	private static Logger LOGGER = LoggerFactory.getLogger(Nse2Application.class);

	public static void main(String[] args) {
		GreetingInScala greetingInScala = new GreetingInScala();
		SpringApplication.run(Nse2Application.class, args);
		LOGGER.info(greetingInScala.greet());

//		Processor1 processor1 = new Processor1();
//		processor1.process();

//		Processor2 processor2 = new Processor2();
//		processor2.process();

		Processor3 processor3 = new Processor3();
		processor3.process();
	}

}
