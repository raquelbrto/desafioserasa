package com.dev.brito.desafioserasa.dto;

public record PersonRequestDTO(
    String name,
    String email,
    String phone,
    int age,
    String zipCode,
    int score,
    boolean active
) {
   
}
