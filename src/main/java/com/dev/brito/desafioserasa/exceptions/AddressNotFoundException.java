package com.dev.brito.desafioserasa.exceptions;

public class AddressNotFoundException extends RuntimeException {

    public AddressNotFoundException() {
        super("Address not found");
    }

    public AddressNotFoundException(String message) {
        super(message);
    }
}
