package io.codelex.personapp.controllers;

import io.codelex.personapp.entity.Person;
import io.codelex.personapp.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController {

    private final PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Person addPerson(@RequestBody Person person) {
        return service.addPerson(person);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Person> matchPersonByPersonalIdAndBirthDate(
            @RequestParam(required = false) String personalId, LocalDate birthDate
    ) {
        return service.getAllPersonsByPersonalIdOrBirthDate(personalId, birthDate);
    }
}