package com.dev.brito.desafioserasa.exceptions;

public class InvalidScoreException extends RuntimeException {

    public InvalidScoreException() {
        super("Invalid score");
    }

    public InvalidScoreException(String message) {
        super(message);
    }
}
