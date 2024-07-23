package com.example.testcontainers.sandbox;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/persons")
@Validated
@Tag(name = "Persons")
class PersonRestController {

  private final PersonService personService;

  public PersonRestController(final PersonService personService) {
    this.personService = personService;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> softDelete(final @PathVariable("id") UUID id) {
    boolean acknowledged = personService.softDelete(id);
    return new ResponseEntity<>(acknowledged, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<PersonResponse> create(final @RequestBody @Valid PersonRequest request) {

    var person =
        personService.create(
            request.firstName(),
            request.lastName(),
            Country.valueOf(request.country()),
            Language.valueOf(request.language()));
    var responseBody = PersonMapper.MAPPER.mapToResponse(person);
    return ResponseEntity.created(URI.create("/api/v1/persons/%s".formatted(person.getId())))
        .body(responseBody);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonResponse> findById(final @PathVariable("id") UUID id) {
    var personResponse =
        personService
            .byId(id)
            .map(PersonMapper.MAPPER::mapToResponse)
            .orElseThrow(NoSuchElementException::new);
    return ResponseEntity.ok(personResponse);
  }
}
