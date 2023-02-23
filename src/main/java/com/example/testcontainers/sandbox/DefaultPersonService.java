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
  public boolean softDelete(final Long id) {
    if (repository.existsById(id)) {
      var person = repository.getReferenceById(id);
      var instant = Instant.now(Clock.systemUTC());
      person.setDeletedAt(instant);
      repository.saveAndFlush(person);
      return true;
    }
    return false;
  }

  @Override
  public Person create(String firstName, String lastName, Country country, Language language) {
    var person = new Person(firstName, lastName, country, language);
    var instant = Instant.now(Clock.systemUTC());
    person.setCreatedAt(instant);
    person.setUpdatedAt(instant);
    return repository.saveAndFlush(person);
  }

  @Override
  public Optional<Person> byId(Long id) {
    return repository.findById(id);
  }
}
