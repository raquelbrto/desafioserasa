package com.dev.brito.desafioserasa.client;

import com.dev.brito.desafioserasa.dto.AddressViaCepDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "${viacep.api.url}")
public interface ViaCepApiClient {

    @GetMapping("/{cep}/json/")
    AddressViaCepDTO findAddressViaCep(@PathVariable("cep") String cep);
}
