package com.example.testcontainers.sandbox;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "person")
public class Person implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, name = "first_name")
  private String firstName;

  @Column(nullable = false, name = "last_name")
  private String lastName;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Country country;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Language language;

  @Column(nullable = false, name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  public Person() {}

  public Person(String firstName, String lastName, Country country, Language language) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.country = country;
    this.language = language;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public Language getLanguage() {
    return language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(Instant deletedAt) {
    this.deletedAt = deletedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (!(o instanceof Person)) return false;

    Person other = (Person) o;

    return this.getId() != null && this.getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
