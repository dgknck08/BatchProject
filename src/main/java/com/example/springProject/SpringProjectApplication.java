package com.example.springProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class SpringProjectApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        // DEBUG: .env'den okunan değerler
        System.out.println("DB URL: " + dotenv.get("SPRING_DATASOURCE_URL"));
        System.out.println("DB Username: " + dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.out.println("DB Password: " + dotenv.get("SPRING_DATASOURCE_PASSWORD"));
        System.out.println("Redmine API Key: " + dotenv.get("REDMINE_API_KEY"));
        System.out.println("Mail Username: " + dotenv.get("SPRING_MAIL_USERNAME"));
        System.out.println("Mail Password: " + dotenv.get("SPRING_MAIL_PASSWORD"));
        System.out.println("Email Receiver: " + dotenv.get("EMAIL_RECEIVER"));

        // System property ayarları
        System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));

        System.setProperty("REDMINE_API_KEY", dotenv.get("REDMINE_API_KEY"));

        System.setProperty("SPRING_MAIL_USERNAME", dotenv.get("SPRING_MAIL_USERNAME"));
        System.setProperty("SPRING_MAIL_PASSWORD", dotenv.get("SPRING_MAIL_PASSWORD"));
        System.setProperty("EMAIL_RECEIVER", dotenv.get("EMAIL_RECEIVER"));

        System.out.println("Environment variables loaded and set as system properties!");

        SpringApplication.run(SpringProjectApplication.class, args);
    }
}
