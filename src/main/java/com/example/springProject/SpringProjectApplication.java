package com.example.springProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableScheduling  
public class SpringProjectApplication {
	

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();

        // Set system properties from environment variables
        System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        
        System.setProperty("REDMINE_API_KEY", dotenv.get("REDMINE_API_KEY"));
        
        System.setProperty("SPRING_MAIL_USERNAME", dotenv.get("SPRING_MAIL_USERNAME"));
        System.setProperty("SPRING_MAIL_PASSWORD", dotenv.get("SPRING_MAIL_PASSWORD"));
        System.setProperty("EMAIL_RECEIVER", dotenv.get("EMAIL_RECEIVER"));
        
        // Now Spring will use these properties if they are referenced
        System.out.println("Environment variables loaded and set as system properties!");
		
		SpringApplication.run(SpringProjectApplication.class, args);
		
	}

}
