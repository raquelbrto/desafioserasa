package com.dev.brito.desafioserasa.dto;

public record AddressBrasilApiDTO(
    String cep,
    String state,
    String city, 
    String neighborhood, 
    String street
) {
}
