package com.dev.brito.desafioserasa.dto;

public record PersonResponseDTO(
    Long id,
    String name,
    String email,
    String phone,
    int age,
    String city,
    String state,
    String zipCode,
    String street,
    String neighborhood,
    int score,
    String scoreDescription
) {
}
