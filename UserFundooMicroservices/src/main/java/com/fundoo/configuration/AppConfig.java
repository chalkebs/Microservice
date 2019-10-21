package com.fundoo.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig
{
	@Bean
	public PasswordEncoder passwordEncoder() 
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public ModelMapper modelMapper() 
	{
	    ModelMapper m = new ModelMapper();
	    m.getConfiguration()
	     .setMatchingStrategy(MatchingStrategies.STRICT);
		return m;
	}
	
}
