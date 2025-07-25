package com.dev.brito.desafioserasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableFeignClients
@EnableCaching
@SpringBootApplication
public class DesafioserasaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioserasaApplication.class, args);
	}

}
