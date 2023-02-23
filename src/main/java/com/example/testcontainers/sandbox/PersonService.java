package com.example.testcontainers.sandbox;

import java.util.Optional;

public interface PersonService {

  boolean softDelete(final Long id);

  Person create(
      final String firstName,
      final String lastName,
      final Country country,
      final Language language);

  Optional<Person> byId(final Long id);
}
