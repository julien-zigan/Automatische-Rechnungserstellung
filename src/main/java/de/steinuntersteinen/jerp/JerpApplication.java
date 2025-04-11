package de.steinuntersteinen.jerp;

import de.steinuntersteinen.jerp.core.AppFacade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
 @EnableConfigurationProperties(StorageProperties.class)
public class JerpApplication {
	public static AppFacade app;

	public static void main(String[] args) {
		app = new AppFacade();
		SpringApplication.run(JerpApplication.class, args);
	}
}
