package io.codelex.personapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codelex.personapp.entity.Gender;
import io.codelex.personapp.entity.Person;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PersonControllerTest {

    static ObjectMapper jsonObjectMapper = new ObjectMapper();
    @Autowired
    PersonController personController;
    @Autowired
    private MockMvc mvc;

    private Person createPerson() {
        return new Person(
                "Simon", "Jarret", "1111", LocalDate.parse("1990-01-01"), Gender.M
        );
    }

    private MockHttpServletRequestBuilder putRequestBuilder(Person person) throws JsonProcessingException {
        return put("/api/v1/person", person)
                .content(jsonObjectMapper.writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder getRequestBuilder(Person person) throws JsonProcessingException {
        return get("/api/v1/person/search", person)
                .content(jsonObjectMapper.writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON);
    }

    private MvcResult mockResult(Integer statusCode, MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mvc.perform(requestBuilder)
                .andExpect(status().is(statusCode))
                .andReturn();
    }

    @Test
    @Rollback
    @DisplayName("Should add and return person with status code 201")
    public void shouldAddPerson() throws Exception {
        Person createdPerson = createPerson();
        jsonObjectMapper.findAndRegisterModules();

        MockHttpServletRequestBuilder requestBuilder = putRequestBuilder(createdPerson);
        MvcResult result = mockResult(201, requestBuilder);
        String responseContents = result.getResponse().getContentAsString();
        Person savedPerson = jsonObjectMapper.readValue(responseContents, Person.class);

        assertNotNull(savedPerson.getPersonalId());
        assertEquals(savedPerson.getBirthDate(), createdPerson.getBirthDate());
        assertEquals(savedPerson.getFirstName(), createdPerson.getFirstName());
        assertEquals(savedPerson.getLastName(), createdPerson.getLastName());
        assertEquals(savedPerson.getPersonalId(), createdPerson.getPersonalId());
    }

    @Test
    @Rollback
    @DisplayName("Should not fail when person already exists")
    public void shouldNotFailOnDuplicatePerson() throws Exception {
        String expected = "Person already exists";
        Person createdPerson = createPerson();
        jsonObjectMapper.findAndRegisterModules();

        personController.addPerson(createdPerson);
        MockHttpServletRequestBuilder requestBuilder = putRequestBuilder(createdPerson);
        MvcResult result = mockResult(201, requestBuilder);
        String responseContent = result.getResponse().getContentAsString();

        assertEquals(expected, responseContent);
    }

    @Test
    @Rollback
    @DisplayName("Should match person by personal id and birthDate")
    void matchPersonByPersonalIdAndBirthDate() throws Exception {
        Person mockPerson = createPerson();
        String personalId = mockPerson.getPersonalId();
        LocalDate birthDate = mockPerson.getBirthDate();
        jsonObjectMapper.findAndRegisterModules();
        List<Person> mockPersons = List.of(mockPerson);
        personController.addPerson(mockPerson);

        MockHttpServletRequestBuilder requestBuilder = getRequestBuilder(mockPerson)
                .param("personalId", personalId)
                .param("birthDate", birthDate.toString());
        MvcResult result = mockResult(200, requestBuilder);
        String responseContents = result.getResponse().getContentAsString();
        List<Person> persons = jsonObjectMapper.readValue(responseContents, new TypeReference<>() {});

        assertNotNull(persons);
        assertEquals(persons.get(0), mockPerson);
        assertEquals(persons, mockPersons);
    }

    @Test
    @Rollback
    @DisplayName("Should match person by personal id")
    void matchPersonByPersonalId() throws Exception {
        Person mockPerson = createPerson();
        String personalId = mockPerson.getPersonalId();
        jsonObjectMapper.findAndRegisterModules();
        List<Person> mockPersons = List.of(mockPerson);
        personController.addPerson(mockPerson);

        MockHttpServletRequestBuilder requestBuilder = getRequestBuilder(mockPerson)
                .param("personalId", personalId);
        MvcResult result = mockResult(200, requestBuilder);
        String responseContents = result.getResponse().getContentAsString();
        List<Person> persons = jsonObjectMapper.readValue(responseContents, new TypeReference<>() {});

        assertNotNull(persons);
        assertEquals(persons.get(0), mockPerson);
        assertEquals(persons, mockPersons);
    }

    @Test
    @Rollback
    @DisplayName("Should match person by birth date")
    void matchPersonByBirthDate() throws Exception {
        Person mockPerson = createPerson();
        LocalDate birthDate = mockPerson.getBirthDate();
        jsonObjectMapper.findAndRegisterModules();
        List<Person> mockPersons = List.of(mockPerson);
        personController.addPerson(mockPerson);

        MockHttpServletRequestBuilder requestBuilder = getRequestBuilder(mockPerson)
                .param("birthDate", birthDate.toString());
        MvcResult result = mockResult(200, requestBuilder);
        String responseContents = result.getResponse().getContentAsString();
        List<Person> persons = jsonObjectMapper.readValue(responseContents, new TypeReference<>() {});

        assertNotNull(persons);
        assertEquals(persons.get(0), mockPerson);
        assertEquals(persons, mockPersons);
    }

    @Test
    @Rollback
    @DisplayName("Should return 200 even if nothing found")
    void ShouldNotFailIfNothingFound() throws Exception {
        Person mockPerson = createPerson();
        LocalDate birthDate = mockPerson.getBirthDate();
        jsonObjectMapper.findAndRegisterModules();

        MockHttpServletRequestBuilder requestBuilder = getRequestBuilder(mockPerson)
                .param("birthDate", birthDate.toString());
        MvcResult result = mockResult(200, requestBuilder);
        String responseContents = result.getResponse().getContentAsString();
        List<Person> persons = jsonObjectMapper.readValue(responseContents, new TypeReference<>() {});

        assertEquals(persons, new ArrayList<Person>());
    }
}