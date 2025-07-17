package com.dev.brito.desafioserasa.mapper;

import com.dev.brito.desafioserasa.dto.AddressBrasilApiDTO;
import com.dev.brito.desafioserasa.dto.AddressDTO;
import com.dev.brito.desafioserasa.dto.AddressViaCepDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    
    @Mapping(source = "cep", target = "zipCode")
    @Mapping(source = "logradouro", target = "street")
    @Mapping(source = "bairro", target = "neighborhood")
    @Mapping(source = "localidade", target = "city")
    @Mapping(source = "uf", target = "state")
    AddressDTO toAddressDTO(AddressViaCepDTO viaCepDto);

    @Mapping(source = "cep", target = "zipCode")
    AddressDTO toAddressDTO(AddressBrasilApiDTO brasilApiDto);
}
