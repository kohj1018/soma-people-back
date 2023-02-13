package kr.somapeople.somapeopleback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SomaPeopleBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SomaPeopleBackApplication.class, args);
	}

}
