package com.dev.brito.desafioserasa.service;

import com.dev.brito.desafioserasa.dto.AddressDTO;
import com.dev.brito.desafioserasa.dto.PersonRequestDTO;
import com.dev.brito.desafioserasa.dto.PersonResponseDTO;
import com.dev.brito.desafioserasa.entity.Person;
import com.dev.brito.desafioserasa.exceptions.PersonAlreadyActiveException;
import com.dev.brito.desafioserasa.exceptions.PersonAlreadyInactiveException;
import jakarta.persistence.EntityNotFoundException;
import com.dev.brito.desafioserasa.mapper.PersonMapper;
import com.dev.brito.desafioserasa.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private PersonService personService;

    private PersonRequestDTO sampleRequest;
    private PersonResponseDTO sampleResponse;
    private AddressDTO sampleAddress;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        sampleRequest = new PersonRequestDTO(
                "Raquel",
                "raquel.aves@gmail.com",
                "84994885502",
                27,
                "59215000",
                1000
        );

        sampleResponse = new PersonResponseDTO(
                1L,
                "Raquel",
                "raquel.aves@gmail.com",
                "84994885502",
                27,
                "Nova Cruz",
                "RN",
                "59215000",
                "",
                "",
                1000,
                "Recomendável"
        );

        sampleAddress = mock(AddressDTO.class);
        when(sampleAddress.city()).thenReturn("Nova Cruz");
        when(sampleAddress.state()).thenReturn("RN");
        when(sampleAddress.street()).thenReturn("");
        when(sampleAddress.neighborhood()).thenReturn("");
    }

    @Test
    void shouldCreatePersonSuccessfully() {
        Person personEntity = new Person();
        Person savedPerson = new Person();

        when(personMapper.toEntity(sampleRequest)).thenReturn(personEntity);
        when(addressService.findAddress(sampleRequest.zipCode())).thenReturn(sampleAddress);
        when(personRepository.save(personEntity)).thenReturn(savedPerson);
        when(personMapper.toResponseDTO(savedPerson)).thenReturn(sampleResponse);

        PersonResponseDTO result = personService.createPerson(sampleRequest);

        assertThat(result).isEqualTo(sampleResponse);
        verify(personRepository).save(personEntity);
    }

    @Test
    void shouldActivateInactivePerson() {
        Person person = new Person();
        person.setActive(false);
        Person updatedPerson = new Person();

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(updatedPerson);
        when(personMapper.toResponseDTO(updatedPerson)).thenReturn(sampleResponse);

        PersonResponseDTO result = personService.activatePerson(1L);

        assertThat(result).isEqualTo(sampleResponse);
        assertThat(person.getActive()).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenActivatingAlreadyActivePerson() {
        Person person = new Person();
        person.setActive(true);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        assertThatThrownBy(() -> personService.activatePerson(1L))
                .isInstanceOf(PersonAlreadyActiveException.class);
    }

    @Test
    void shouldReturnAllPersons() {
        Person person = new Person();
        List<Person> persons = Arrays.asList(person);

        when(personRepository.findAll()).thenReturn(persons);
        when(personMapper.toResponseDTO(person)).thenReturn(sampleResponse);

        List<PersonResponseDTO> result = personService.getAllPersons();

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldReturnActivePersonById() {
        Person person = new Person();

        when(personRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(person));
        when(personMapper.toResponseDTO(person)).thenReturn(sampleResponse);

        Optional<PersonResponseDTO> result = personService.getPersonById(1L);

        assertThat(result).isPresent();
    }

    @Test
    void shouldDeactivateActivePerson() {
        Person person = new Person();
        person.setActive(true);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        personService.deletePerson(1L);

        verify(personRepository).save(person);
        assertThat(person.getActive()).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenDeactivatingAlreadyInactivePerson() {
        Person person = new Person();
        person.setActive(false);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        assertThatThrownBy(() -> personService.deletePerson(1L))
                .isInstanceOf(PersonAlreadyInactiveException.class);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPerson() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personService.deletePerson(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldUpdatePersonSuccessfully() {
        Person person = new Person();
        Person updatedPerson = new Person();

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(addressService.findAddress(sampleRequest.zipCode())).thenReturn(sampleAddress);
        when(personRepository.save(person)).thenReturn(updatedPerson);
        when(personMapper.toResponseDTO(updatedPerson)).thenReturn(sampleResponse);

        doNothing().when(personMapper).updateEntityFromRequest(sampleRequest, person);

        PersonResponseDTO result = personService.updatePerson(1L, sampleRequest);

        assertThat(result).isEqualTo(sampleResponse);
    }

    @Test
    void shouldFilterPersonsWithPagination() {
        Person person = new Person();
        Page<Person> page = new PageImpl<>(List.of(person));
        Pageable pageable = PageRequest.of(0, 10);

        when(personRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(personMapper.toResponseDTO(person)).thenReturn(sampleResponse);

        Page<PersonResponseDTO> result = personService.filterPersons("Raquel", 20, "59215000", pageable);

        assertThat(result.getContent()).hasSize(1);
    }
}
