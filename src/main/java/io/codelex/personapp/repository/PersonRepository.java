package io.codelex.personapp.repository;

import io.codelex.personapp.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByPersonalIdOrBirthDate(String personalId, LocalDate birthDate);

    boolean existsByPersonalId(String personalId);

}
