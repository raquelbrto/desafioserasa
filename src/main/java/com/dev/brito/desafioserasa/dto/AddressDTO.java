package com.dev.brito.desafioserasa.dto;

public record AddressDTO(
    String city,
    String state,
    String zipCode,
    String street,
    String neighborhood
) {
}
