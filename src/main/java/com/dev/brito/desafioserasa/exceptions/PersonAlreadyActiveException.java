package com.dev.brito.desafioserasa.exceptions;

public class PersonAlreadyActiveException extends RuntimeException {

    public PersonAlreadyActiveException() {
        super("Person already active");
    }

    public PersonAlreadyActiveException(String message) {
        super(message);
    }
}