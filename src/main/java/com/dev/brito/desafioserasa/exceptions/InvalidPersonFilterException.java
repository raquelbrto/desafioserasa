package com.dev.brito.desafioserasa.exceptions;

public class InvalidPersonFilterException extends RuntimeException {

    public InvalidPersonFilterException() {
        super("Invalid filter query");
    }
    public InvalidPersonFilterException(String message) {
        super(message);
    }
}