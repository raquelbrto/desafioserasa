package com.dev.brito.desafioserasa.mapper;

import com.dev.brito.desafioserasa.dto.PersonRequestDTO;
import com.dev.brito.desafioserasa.dto.PersonResponseDTO;
import com.dev.brito.desafioserasa.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person toEntity(PersonRequestDTO dto);

    PersonResponseDTO toResponseDTO(Person person);

    void updateEntityFromRequest(PersonRequestDTO dto, @MappingTarget Person person);
}
