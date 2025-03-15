package com.ceos21.spring_boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext context) {
		return args -> {
			System.out.println("beans provided by Spring Boot");

			String[] beanDefinitionNames = context.getBeanDefinitionNames();
			Arrays.sort(beanDefinitionNames);
			for (String beanName : beanDefinitionNames) {
				System.out.println(beanName);
			}
		};
	}

}
