package io.codelex.personapp.services;

import io.codelex.personapp.exception.PersonAlreadyExistsException;
import io.codelex.personapp.entity.Gender;
import io.codelex.personapp.entity.Person;
import io.codelex.personapp.repository.PersonRepository;
import io.codelex.personapp.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    PersonRepository personRepository;

    @InjectMocks
    PersonService personService;

    private Person createPerson() {
        return new Person(
                "Simon", "Jarret", "1111", LocalDate.parse("1990-01-01"), Gender.M
        );
    }

    @Test
    public void ShouldSavePerson() {
        Person person = createPerson();
        personService.addPerson(person);
        verify(personRepository).save(person);
    }

    @Test
    public void shouldThrowIfPersonAlreadyExists() {
        Person person = createPerson();
        when(personRepository.existsByPersonalId(person.getPersonalId())).thenReturn(true);
        assertThrows(PersonAlreadyExistsException.class,
                () -> personService.addPerson(person)
        );
    }

    @Test
    void shouldReturnNothingWhenEmptyQueryFields() {
        List<Person> emptyMock = List.of(createPerson());

        when(personRepository.findAllByPersonalIdOrBirthDate(null, null))
                .thenReturn(emptyMock);

        List<Person> result = personService.getAllPersonsByPersonalIdOrBirthDate(null, null);
        assertEquals(emptyMock, result);
    }

    @Test
    void shouldQueryForBirthDateAndOrPersonalId() {
        List<Person> mockPersons1 = List.of(
                createPerson(),
                new Person("Catherine", "Chun", "2222", LocalDate.parse("1991-02-02"), Gender.F)
        );
        List<Person> mockPersons2 = new ArrayList<>(mockPersons1);
        List<Person> mockPersons3 = new ArrayList<>(mockPersons1);

        String personalId1 = "1111";
        String personalId2 = "2222";
        LocalDate birthDate1 = LocalDate.parse("1990-01-01");
        LocalDate birthDate2 = LocalDate.parse("1991-02-02");

        when(personRepository.findAllByPersonalIdOrBirthDate(personalId1, birthDate1))
                .thenReturn(mockPersons1);
        when(personRepository.findAllByPersonalIdOrBirthDate(null, birthDate2))
                .thenReturn(mockPersons2);
        when(personRepository.findAllByPersonalIdOrBirthDate(personalId2, null))
                .thenReturn(mockPersons3);

        List<Person> result1 = personService.getAllPersonsByPersonalIdOrBirthDate(personalId1, birthDate1);
        List<Person> result2 = personService.getAllPersonsByPersonalIdOrBirthDate(null, birthDate2);
        List<Person> result3 = personService.getAllPersonsByPersonalIdOrBirthDate(personalId2, null);

        assertEquals(mockPersons1, result1);
        assertEquals(mockPersons2, result2);
        assertEquals(mockPersons3, result3);
    }
}