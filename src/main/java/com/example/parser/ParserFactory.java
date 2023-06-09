package com.example.parser;

import com.example.domain.entity.Hospital;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserFactory {

	@Bean
	public ReadLineContext<Hospital> hospitalReadLineContext() {

		return new ReadLineContext<>(new HospitalParser());

	}

}
