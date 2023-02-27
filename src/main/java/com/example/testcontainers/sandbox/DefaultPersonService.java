package com.example.testcontainers.sandbox;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DefaultPersonService implements PersonService {

  private final PersonRepository repository;

  public DefaultPersonService(PersonRepository repository) {
    this.repository = repository;
  }

  @Override
  public boolean softDelete(final UUID id) {
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
    return repository.saveAndFlush(new Person(firstName, lastName, country, language));
  }

  @Override
  public Optional<Person> byId(UUID id) {
    return repository.findById(id);
  }
}
