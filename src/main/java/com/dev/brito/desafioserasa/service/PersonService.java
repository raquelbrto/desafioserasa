package com.dev.brito.desafioserasa.service;

import com.dev.brito.desafioserasa.dto.AddressDTO;
import com.dev.brito.desafioserasa.dto.PersonRequestDTO;
import com.dev.brito.desafioserasa.dto.PersonResponseDTO;
import com.dev.brito.desafioserasa.entity.Person;
import com.dev.brito.desafioserasa.enums.ScoreDescription;
import com.dev.brito.desafioserasa.mapper.PersonMapper;
import com.dev.brito.desafioserasa.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
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
        person.setScoreDescription(scoreDescription(personRequestDTO.score()));
        buildAddressPerson(person, personRequestDTO.zipCode());

        Person personSaved = personRepository.save(person);
        return personMapper.toResponseDTO(personSaved);
    }

    public List<PersonResponseDTO> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return persons.stream()
                .map(personMapper::toResponseDTO)
                .toList();
    }

    public Optional<PersonResponseDTO> getPersonById(Long id) {
        return personRepository.findById(id)
                .map(personMapper::toResponseDTO);
    }

    public void deletePerson(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));

        if (Boolean.FALSE.equals(person.getActive())) {
            throw new IllegalStateException("Person is already inactive");
        }

        person.setActive(false);
        personRepository.save(person);
    }

    public PersonResponseDTO updatePerson(Long id, PersonRequestDTO personRequestDTO) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));

        person.setScoreDescription(scoreDescription(personRequestDTO.score()));
        buildAddressPerson(person, personRequestDTO.zipCode());

        personMapper.updateEntityFromRequest(personRequestDTO, person);
        Person updatedPerson = personRepository.save(person);
        return personMapper.toResponseDTO(updatedPerson);
    }

    public Page<PersonResponseDTO> filterPersons(String name, Integer age, String zipCode, Pageable pageable) {
        Specification<Person> spec = (root, query, builder) -> null;

        if (name != null && !name.isBlank()) {
            spec = spec.and(nameContains(name));
        }

        if (age != null) {
            spec = spec.and(ageEquals(age));
        }

        if (zipCode != null && !zipCode.isBlank()) {
            spec = spec.and(zipCodeEquals(zipCode));
        }

        Page<Person> page = personRepository.findAll(spec, pageable);
        return page.map(personMapper::toResponseDTO);
    }

    private Specification<Person> nameContains(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    private Specification<Person> ageEquals(Integer age) {
        return (root, query, builder) ->
                builder.equal(root.get("age"), age);
    }

    private Specification<Person> zipCodeEquals(String zipCode) {
        return (root, query, builder) ->
                builder.equal(root.get("zipCode"), zipCode);
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
}
