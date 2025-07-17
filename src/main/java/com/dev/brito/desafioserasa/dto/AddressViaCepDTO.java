package com.dev.brito.desafioserasa.dto;

public record AddressViaCepDTO(
    String cep,
    String logradouro,
    String complemento,
    String bairro,
    String localidade,
    String uf
) {
}
