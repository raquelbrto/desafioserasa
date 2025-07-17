package com.dev.brito.desafioserasa.service;

import com.dev.brito.desafioserasa.client.BrasilApiClient;
import com.dev.brito.desafioserasa.client.ViaCepApiClient;
import com.dev.brito.desafioserasa.dto.AddressDTO;
import com.dev.brito.desafioserasa.mapper.AddressMapper;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddressService {

    private final ViaCepApiClient viaCepClient;

    private final BrasilApiClient brasilApiClient;

    private final AddressMapper addressMapper;

    public AddressService(ViaCepApiClient viaCepClient, BrasilApiClient brasilApiClient, AddressMapper addressMapper) {
        this.viaCepClient = viaCepClient;
        this.brasilApiClient = brasilApiClient;
        this.addressMapper = addressMapper;
    }

    @Cacheable("addresses")
    @Retry(name = "address", fallbackMethod = "failBackFindAddress")
    public AddressDTO findAddress(String zipCode) {
        log.info("Buscando endereço na api via cep para CEP: {}", zipCode);

        return addressMapper.toAddressDTO(viaCepClient.findAddressViaCep(zipCode));
    }

    public AddressDTO failBackFindAddress(String zipCode, Exception e) {
        log.error("Fallback acionado apos tentativas de retry para o zipCode: {} - Erro: {}", zipCode, e.getMessage());
        return addressMapper.toAddressDTO(brasilApiClient.findAddressBrasilia(zipCode));
    }
}
