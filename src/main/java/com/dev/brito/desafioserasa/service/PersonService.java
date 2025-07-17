package com.dev.brito.desafioserasa.service;

import com.dev.brito.desafioserasa.dto.AddressDTO;
import com.dev.brito.desafioserasa.dto.PersonRequestDTO;
import com.dev.brito.desafioserasa.dto.PersonResponseDTO;
import com.dev.brito.desafioserasa.entity.Person;
import com.dev.brito.desafioserasa.enums.ScoreDescription;
import com.dev.brito.desafioserasa.exceptions.InvalidPersonFilterException;
import com.dev.brito.desafioserasa.exceptions.PersonAlreadyActiveException;
import com.dev.brito.desafioserasa.exceptions.PersonAlreadyInactiveException;
import com.dev.brito.desafioserasa.exceptions.PersonNotFoundException;
import com.dev.brito.desafioserasa.mapper.PersonMapper;
import com.dev.brito.desafioserasa.repository.PersonRepository;
import com.dev.brito.desafioserasa.specification.PersonSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    private final AddressService addressService;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper, AddressService addressService) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.addressService = addressService;
    }

    public PersonResponseDTO createPerson(PersonRequestDTO personRequestDTO) {
        Person person = personMapper.toEntity(personRequestDTO);
        person.setActive(true);
        person.setScoreDescription(scoreDescription(personRequestDTO.score()));
        buildAddressPerson(person, personRequestDTO.zipCode());

        Person personSaved = personRepository.save(person);
        return personMapper.toResponseDTO(personSaved);
    }

    public void activatePerson(Long id) {
        Person person = findPerson(id);

        if (Boolean.TRUE.equals(person.getActive())) {
            throw new PersonAlreadyActiveException("Person is already active");
        }

        person.setActive(true);
        personRepository.save(person);
    }

    public List<PersonResponseDTO> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return persons.stream()
                .map(personMapper::toResponseDTO)
                .toList();
    }

    public Optional<PersonResponseDTO> getPersonById(Long id) {
        return personRepository.findByIdAndActiveTrue(id).map(personMapper::toResponseDTO);
    }

    public void deletePerson(Long id) {
        Person person = findPerson(id);

        if (Boolean.FALSE.equals(person.getActive())) {
            throw new PersonAlreadyInactiveException("Person already inactive with id: " + id);
        }

        person.setActive(false);
        personRepository.save(person);
    }

    public PersonResponseDTO updatePerson(Long id, PersonRequestDTO personRequestDTO) {
        Person person = findPerson(id);

        person.setScoreDescription(scoreDescription(personRequestDTO.score()));
        buildAddressPerson(person, personRequestDTO.zipCode());

        personMapper.updateEntityFromRequest(personRequestDTO, person);
        Person updatedPerson = personRepository.save(person);
        return personMapper.toResponseDTO(updatedPerson);
    }

    public Page<PersonResponseDTO> filterPersons(String name, Integer age, String zipCode, Pageable pageable) {
        validateFilters(name, age, zipCode);

        Specification<Person> spec = PersonSpecification.build(name, age, zipCode);

        return personRepository.findAll(spec, pageable)
                .map(personMapper::toResponseDTO);
    }

    private String scoreDescription(int score) {
        return ScoreDescription.fromScore(score);
    }

    private void buildAddressPerson(Person person, String zipCode) {
        AddressDTO address = addressService.findAddress(zipCode);
        person.setCity(address.city());
        person.setState(address.state());
        person.setStreet(address.street());
        person.setNeighborhood(address.neighborhood());
    }

    private void validateFilters(String name, Integer age, String zipCode) {
        if (name != null && name.length() > 100) {
            throw new InvalidPersonFilterException("Nome muito longo");
        }

        if (age != null && age < 0) {
            throw new InvalidPersonFilterException("Idade negativa não é permitida");
        }

        if (zipCode != null && zipCode.length() > 8) {
            throw new InvalidPersonFilterException("CEP deve ter no máximo 8 caracteres");
        }
    }

    private Person findPerson(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }
}
