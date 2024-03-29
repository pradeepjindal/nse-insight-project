package org.pra.nse;

import org.fusesource.jansi.AnsiConsole;
import org.pra.nse.calc.SwingAroundMeanAndAverage;
import org.pra.nse.csv.read.EqnCsvReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NseApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(NseApplication.class);

	public static void main(String[] args) {
		AnsiConsole.systemInstall();
		GreetingInScala greetingInScala = new GreetingInScala();

//		MeanAndAverageSwing meanAndAverageSwing = new MeanAndAverageSwing();
//		meanAndAverageSwing.compute2();

//		SwingAroundMeanAndAverage swingAroundMeanAndAverage = new SwingAroundMeanAndAverage();
//		swingAroundMeanAndAverage.compute();

		SpringApplication.run(NseApplication.class, args);
		LOGGER.info(greetingInScala.greet());

		LOGGER.debug("This is a debug message");
		LOGGER.info("This is an info message");
		LOGGER.warn("This is a warn message");
		LOGGER.error("This is an error message");

		LOGGER.info(System.getProperty("user.name"));
		LOGGER.info(System.getProperty("user.home"));
		LOGGER.info(System.getProperty("user.dir"));

		AnsiConsole.systemUninstall();
	}
}
