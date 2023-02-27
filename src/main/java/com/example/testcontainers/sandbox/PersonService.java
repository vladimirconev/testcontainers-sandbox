package com.example.testcontainers.sandbox;

import java.util.Optional;
import java.util.UUID;

public interface PersonService {

  boolean softDelete(final UUID id);

  Person create(
      final String firstName,
      final String lastName,
      final Country country,
      final Language language);

  Optional<Person> byId(final UUID id);
}
