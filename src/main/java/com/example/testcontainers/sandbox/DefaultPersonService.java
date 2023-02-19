package com.example.testcontainers.sandbox;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

public class DefaultPersonService implements PersonService {

  private final PersonRepository repository;

  public DefaultPersonService(PersonRepository repository) {
    this.repository = repository;
  }

  @Override
  public Person create(String firstName, String lastName, Country country, Language language) {
    var person = new Person(firstName, lastName, country, language);
    person.setCreatedAt(Instant.now(Clock.systemUTC()));
    return repository.saveAndFlush(person);
  }

  @Override
  public Optional<Person> byId(Long id) {
    return Optional.ofNullable(repository.getReferenceById(id));
  }
}
