package com.dev.brito.desafioserasa.exceptions;

public class PersonAlreadyInactiveException extends RuntimeException {

    public PersonAlreadyInactiveException() {
        super("Person already inactive");
    }

    public PersonAlreadyInactiveException(String message) {
        super(message);
    }
}
