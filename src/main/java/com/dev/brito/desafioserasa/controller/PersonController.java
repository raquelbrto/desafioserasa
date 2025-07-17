package com.dev.brito.desafioserasa.controller;

import com.dev.brito.desafioserasa.dto.PersonRequestDTO;
import com.dev.brito.desafioserasa.dto.PersonResponseDTO;
import com.dev.brito.desafioserasa.service.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonResponseDTO> createPerson(@RequestBody PersonRequestDTO personRequestDTO) {
        PersonResponseDTO response = personService.createPerson(personRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> getAllPersons() {
        List<PersonResponseDTO> persons = personService.getAllPersons();
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/search-filter")
    public ResponseEntity<Page<PersonResponseDTO>> getPersonsPaged(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String zipCode,
            Pageable pageable) {
        Page<PersonResponseDTO> page = personService.filterPersons(name, age, zipCode, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> updatePerson(
            @PathVariable Long id, 
            @RequestBody PersonRequestDTO personRequestDTO) {
        personService.updatePerson(id, personRequestDTO);
        return ResponseEntity.noContent().build();
    }
}
