package io.codelex.personapp.Exceptions;

public class PersonAlreadyExistsException extends RuntimeException {
    public PersonAlreadyExistsException() {
        super("Person already exists");
    }
}
