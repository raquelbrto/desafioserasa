package com.dev.brito.desafioserasa.controller;

import com.dev.brito.desafioserasa.dto.PersonRequestDTO;
import com.dev.brito.desafioserasa.dto.PersonResponseDTO;
import com.dev.brito.desafioserasa.exceptions.PersonNotFoundException;
import com.dev.brito.desafioserasa.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(
            summary = "Salvar uma nova pessoa",
            description = "Adiciona uma nova pessoa ao sistema e retorna os dados da pessoa criada.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso",
                            content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
            }
    )
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
        PersonResponseDTO person = personService.getPersonById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        return ResponseEntity.ok(person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualizar todos os dados de uma pessoa",
            description = "Atualiza todos os dados de uma pessoa existente pelo seu identificador (id).",
            parameters = {
                    @Parameter(name = "id", description = "ID da pessoa", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso",
                            content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> updateAllPerson(
            @PathVariable Long id,
            @RequestBody PersonRequestDTO personRequestDTO) {
        PersonResponseDTO response = personService.updatePerson(id, personRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<PersonResponseDTO> activatePerson(@PathVariable Long id) {
        PersonResponseDTO activatedPerson = personService.activatePerson(id);
        return ResponseEntity.ok(activatedPerson);
    }
}
