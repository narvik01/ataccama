package cz.strangeloop.ataccama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableSwagger2
public class AtaccamaApplication {

	//TODO cleanup data
	//TODO comments
	//TODO logback config

	public static void main(String[] args) {
		SpringApplication.run(AtaccamaApplication.class, args);
	}

}
