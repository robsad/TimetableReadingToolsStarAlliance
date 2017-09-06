package app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import app.fetch.DataFetch;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EnableJpaRepositories(basePackages = "app.repository")
@EntityScan
@ComponentScan("app")
public class Application implements CommandLineRunner {

	private static final Logger log = LoggerFactory
			.getLogger(Application.class);

	@Autowired
	private DataFetch dataFetch;

	public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}

	@Override
	public void run(String... args) throws Exception {
		dataFetch.initAirports();
		dataFetch.saveAirports(); // zapis portow do bazy
		dataFetch.initConnection();
	}
}