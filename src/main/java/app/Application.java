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
import app.fetch.AirportsScanService;
import app.fetch.ReadingManager;
import app.tofile.SaveFromDBToFileManager;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"app.repository"})
@EntityScan(basePackages = {"app.entities"})
@ComponentScan({"app.*"})
public class Application implements CommandLineRunner {

	@Autowired
	ReadingManager readingManager;
	@Autowired
	SaveFromDBToFileManager saveFromDBToFileManager;
	
	private static final Logger log = LoggerFactory
			.getLogger(Application.class);
	
	public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}

	@Override
	public void run(String... args) throws Exception {
		String alliance = "OneWorld"; // works with: "OneWorld" "StarAlliance" "Etihad"
		readingManager.airportsScan(alliance);
		readingManager.saveAirportsToFile(alliance);
		readingManager.connectionsScan(alliance);
		readingManager.saveConnectionsToFile(alliance);
		//saveFromDBToFileManager.readAirportsFromDatabaseToFile(alliance);
		//saveFromDBToFileManager.readConnectionsFromDatabaseToFile(alliance);
	}
}