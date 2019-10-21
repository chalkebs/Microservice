package com.fundoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = "com.fundoo")
public class NoteFundooApi 
{
	public static void main(String[] args)
	{
		SpringApplication.run(NoteFundooApi.class, args);
	}
	
}
