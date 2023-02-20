package com.example.testcontainers.sandbox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class TestcontainersSandboxApplicationTests {

  @Container
  private static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.2-alpine"));

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
    var payload = new PersonRequest();
    payload.setCountry("BR");
    payload.setLanguage("PT");
    payload.setFirstName("Eder");
    payload.setLastName("Morales");
    MvcResult mvcResult =
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
    assertThat(personResponse.getId()).isNotNull();
  }

  @Test
  void findById() throws Exception {
    var person = personService.create("Cecilia", "Brohmdahl", Country.CH, Language.DE);
    MvcResult mvcResult =
        mockMvc
            .perform(
                get("/api/v1/persons/%d".formatted(person.getId()))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();
    var mockResponse = mvcResult.getResponse();
    assertThat(mockResponse).isNotNull();
    var personResponse =
        objectMapper.readValue(mockResponse.getContentAsString(), PersonResponse.class);
    assertThat(personResponse).isNotNull();
    assertEquals(person.getId(), personResponse.getId());
    assertEquals(person.getCountry().name(), personResponse.getCountry());
    assertEquals(person.getLanguage().name(), personResponse.getLanguage());
    assertEquals(person.getLanguage().name(), personResponse.getLanguage());
    assertEquals(person.getLanguage().name(), personResponse.getLanguage());
  }
}
