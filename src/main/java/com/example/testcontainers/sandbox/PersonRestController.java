package com.example.testcontainers.sandbox;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/persons")
@Validated
public class PersonRestController {

  private final PersonService personService;

  public PersonRestController(PersonService personService) {
    this.personService = personService;
  }

  @PostMapping
  public ResponseEntity<PersonResponse> create(final @RequestBody @Valid PersonRequest request) {

    var person =
        personService.create(
            request.getFirstName(),
            request.getLastName(),
            Country.valueOf(request.getCountry()),
            Language.valueOf(request.getLanguage()));
    var responseBody = PersonMapper.MAPPER.fromPerson(person);
    return ResponseEntity.created(URI.create("/api/v1/persons/%d".formatted(person.getId())))
        .body(responseBody);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonResponse> findById(final @PathVariable("id") Long id) {
    var personResponse =
        personService
            .byId(id)
            .map(PersonMapper.MAPPER::fromPerson)
            .orElseThrow(NoSuchElementException::new);
    return ResponseEntity.ok(personResponse);
  }
}
