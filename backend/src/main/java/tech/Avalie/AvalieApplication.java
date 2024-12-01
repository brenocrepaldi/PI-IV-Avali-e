package tech.Avalie;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tech.Avalie.ServerValidation.ValidationServer;

import java.io.IOException;

@SpringBootApplication
public class AvalieApplication {

	public static void main(String[] args) {
		SpringApplication.run(AvalieApplication.class, args);
	}
	@PostConstruct
	public void startValidationServer() {
		try {
			ValidationServer validationServer = new ValidationServer(12345);
			new Thread(() -> validationServer.start()).start();
			System.out.println("Servidor de validação iniciado...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}