package io.codelex.personapp.exception;

public class PersonAlreadyExistsException extends RuntimeException {
    public PersonAlreadyExistsException() {
        super("Person already exists");
    }
}
