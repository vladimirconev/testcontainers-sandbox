package com.example.testcontainers.sandbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class TestcontainersSandboxApplicationTests {

  static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
      new PostgreSQLContainer<>("postgres:15.2-alpine").withReuse(true);

  @Autowired private PersonRestController personRestController;

  @Autowired private PersonService personService;

  @Autowired private ObjectMapper objectMapper;
  @Autowired private MockMvc mockMvc;

  @BeforeAll
  static void setup() {
    POSTGRES_SQL_CONTAINER.start();
  }

  @AfterAll
  static void tearDown() {
    POSTGRES_SQL_CONTAINER.close();
    assertThat(POSTGRES_SQL_CONTAINER.isRunning()).isFalse();
  }

  @BeforeEach
  void testIsContainerRunning() {
    assertThat(POSTGRES_SQL_CONTAINER.isRunning()).isTrue();
  }

  @Test
  void contextLoads() {
    assertThat(personRestController).isNotNull();
    assertThat(mockMvc).isNotNull();
    assertThat(personService).isNotNull();
  }

  @Test
  void createPerson() throws Exception {
    var payload = new PersonRequest("Eder", "Morales", "BR", "PT");
    var mvcResult =
        mockMvc
            .perform(
                post("/api/v1/persons")
                    .content(objectMapper.writeValueAsString(payload))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn();
    var mockResponse = mvcResult.getResponse();
    assertThat(mockResponse).isNotNull();
    var personResponse =
        objectMapper.readValue(mockResponse.getContentAsString(), PersonResponse.class);
    assertThat(personResponse).isNotNull();
    assertThat(personResponse.id()).isNotNull();
  }

  @Test
  void findById() throws Exception {
    var person = personService.create("Cecilia", "Brohmdahl", Country.CH, Language.DE);
    MvcResult mvcResult =
        mockMvc
            .perform(
                get("/api/v1/persons/%s".formatted(person.getId().toString()))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    var mockResponse = mvcResult.getResponse();
    assertThat(mockResponse).isNotNull();
    var personResponse =
        objectMapper.readValue(mockResponse.getContentAsString(), PersonResponse.class);
    assertThat(personResponse).isNotNull();
    assertEquals(person.getId(), personResponse.id());
    assertEquals(person.getCountry().name(), personResponse.country());
    assertEquals(person.getLanguage().name(), personResponse.language());
    assertEquals(person.getFirstName(), personResponse.firstName());
    assertEquals(person.getLastName(), personResponse.lastName());
  }

  @Test
  void softDelete() throws Exception {
    var person = personService.create("Stjepan", "Vrsaljko", Country.CH, Language.IT);

    MvcResult mvcResult =
        mockMvc
            .perform(
                delete("/api/v1/persons/%s".formatted(person.getId().toString()))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    var mockResponse = mvcResult.getResponse();
    assertThat(mockResponse).isNotNull();
    var contentAsString = mockResponse.getContentAsString();
    assertEquals("true", contentAsString);
  }

  @Test
  void softDeleteOnNonExistingElementShouldNotBeAcknowledged() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(
                delete("/api/v1/persons/%s".formatted(UUID.randomUUID().toString()))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    var mockResponse = mvcResult.getResponse();
    assertThat(mockResponse).isNotNull();
    var contentAsString = mockResponse.getContentAsString();
    assertEquals("false", contentAsString);
  }
}
