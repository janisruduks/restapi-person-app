package io.codelex.personapp.service;

import io.codelex.personapp.exception.PersonAlreadyExistsException;
import io.codelex.personapp.entity.Person;
import io.codelex.personapp.repository.PersonRepository;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PersonService {

    private final PersonRepository repository;
    private static final Logger LOGGER = LogManager.getLogger(PersonService.class);

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public Person addPerson(Person person) {
        if (repository.existsByPersonalId(person.getPersonalId())) {
            LOGGER.info(person + " person already exists!");
            throw new PersonAlreadyExistsException();
        }
        LOGGER.info("Saving person " + person);
        return repository.save(person);
    }

    public List<Person> getAllPersonsByPersonalIdOrBirthDate(String personalId, LocalDate birthDate) {
        LOGGER.info("Querying for person with data,");
        return repository.findAllByPersonalIdOrBirthDate(personalId, birthDate);
    }
}