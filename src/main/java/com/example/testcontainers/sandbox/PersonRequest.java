package com.example.testcontainers.sandbox;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

public class PersonRequest implements Serializable {

  @NotNull(message = "First name can not be null.")
  private String firstName;

  @NotNull(message = "Last name can not be null.")
  private String lastName;

  @Pattern(
      regexp = "CH" + "|BR" + "|PT" + "|FR",
      message = "Supported values for country field:" + "CH" + "|BR" + "|FR" + "|PT.")
  private String country;

  @Pattern(
      regexp = "DE" + "|IT" + "|FR" + "|EN" + "|PT",
      message = "Supported values for language field:" + "DE" + "|IT" + "|FR" + "|EN" + "|PT.")
  private String language;

  public PersonRequest() { // making Jackson happy
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

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }
}
