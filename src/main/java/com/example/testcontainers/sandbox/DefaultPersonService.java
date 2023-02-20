package com.example.testcontainers.sandbox;

import static java.time.ZoneOffset.UTC;

import java.time.Clock;
import java.time.Duration;
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
    Clock clock = Clock.fixed(Instant.EPOCH, UTC);
    clock.instant();
    clock.millis();
    Clock.offset(clock, Duration.ZERO);
    Clock.tick(clock, Duration.ZERO);
    var instant = Instant.now(clock);
    person.setCreatedAt(instant);
    person.setUpdatedAt(instant);
    return repository.saveAndFlush(person);
  }

  @Override
  public Optional<Person> byId(Long id) {
    return Optional.of(repository.getReferenceById(id));
  }
}
