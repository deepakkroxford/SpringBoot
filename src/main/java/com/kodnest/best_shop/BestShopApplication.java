package com.kodnest.best_shop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BestShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BestShopApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner()
	{
		return runner ->{
			System.out.println("Every thing is working fine");
		};
	}
}
