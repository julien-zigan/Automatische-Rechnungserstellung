package de.steinuntersteinen.jerp;

import de.steinuntersteinen.jerp.core.AppFacade;
import de.steinuntersteinen.jerp.storage.StorageProperties;
import de.steinuntersteinen.jerp.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
 @EnableConfigurationProperties(StorageProperties.class)
public class JerpApplication {
	public static AppFacade app;

	public static void main(String[] args) {
		app = new AppFacade();
		SpringApplication.run(JerpApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(StorageService storageService) {
		return args -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}
