package com.dev.brito.desafioserasa.client;

import com.dev.brito.desafioserasa.dto.AddressBrasilApiDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "brasilapi", url = "${brasil.api.url}")
public interface BrasilApiClient {

    @GetMapping("cep/v1/{cep}")
    AddressBrasilApiDTO findAddressBrasilia(@PathVariable("cep") String cep);
}
