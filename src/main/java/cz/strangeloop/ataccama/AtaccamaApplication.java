package cz.strangeloop.ataccama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AtaccamaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtaccamaApplication.class, args);
	}

}
