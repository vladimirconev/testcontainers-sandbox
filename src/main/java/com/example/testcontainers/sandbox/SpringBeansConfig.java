package com.example.testcontainers.sandbox;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBeansConfig {

  @Bean
  public PersonService personService(final PersonRepository personRepository) {
    return new DefaultPersonService(personRepository);
  }
}
