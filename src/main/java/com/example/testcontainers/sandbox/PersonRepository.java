package com.example.testcontainers.sandbox;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, UUID> {

  @Query(
      value = "SELECT * FROM person AS p WHERE p.id =:id AND p.deleted_at IS NULL",
      nativeQuery = true)
  Optional<Person> findById(final UUID id);
}
