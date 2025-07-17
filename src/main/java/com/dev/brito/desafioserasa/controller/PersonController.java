package com.dev.brito.desafioserasa.controller;

import com.dev.brito.desafioserasa.config.security.SecurityConfigurations;
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

@Tag(
        name = "Person",
        description = "Operações relacionadas à entidade Person"
)
@RestController
@RequestMapping("/api/v1/persons")
@SecurityRequirement(name = SecurityConfigurations.SECURITY)
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

    @Operation(
            summary = "Buscar pessoas com filtro e paginação",
            description = "Retorna uma página de pessoas filtrando por nome, idade e/ou CEP. Todos os filtros são opcionais.",
            parameters = {
                    @Parameter(name = "name", description = "Nome da pessoa para filtro (opcional)", example = "Raquel"),
                    @Parameter(name = "age", description = "Idade da pessoa para filtro (opcional)", example = "27"),
                    @Parameter(name = "zipCode", description = "CEP da pessoa para filtro (opcional)", example = "59215000"),
                    @Parameter(name = "page", description = "Número da página (opcional)", example = "0"),
                    @Parameter(name = "size", description = "Tamanho da página (opcional)", example = "10"),
                    @Parameter(name = "sort", description = "Ordenação (opcional)", example = "name,asc")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Página de pessoas retornada com sucesso",
                            content = @Content(schema = @Schema(implementation = org.springframework.data.domain.Page.class))),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Filtro invalido", content = @Content)
            }
    )
    @GetMapping("/search-filter")
    public ResponseEntity<Page<PersonResponseDTO>> getPersonsPaged(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String zipCode,
            Pageable pageable) {
        Page<PersonResponseDTO> page = personService.filterPersons(name, age, zipCode, pageable);
        return ResponseEntity.ok(page);
    }

    @Operation(
            summary = "Buscar pessoa por ID",
            description = "Retorna os dados de uma pessoa ativa pelo seu identificador(id).",
            parameters = {
                    @Parameter(name = "id", description = "ID da pessoa", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pessoa encontrada com sucesso",
                            content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> getPersonById(@PathVariable Long id) {
        PersonResponseDTO person = personService.getPersonById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        return ResponseEntity.ok(person);
    }

    @Operation(
            summary = "Deletar (inativar) uma pessoa",
            description = "Inativa uma pessoa pelo seu identificador (id). Lança erro se a pessoa não existir ou já estiver inativa.",
            parameters = {
                    @Parameter(name = "id", description = "ID da pessoa", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Pessoa inativada com sucesso", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Pessoa já está inativa", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
            }
    )
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
    public ResponseEntity<PersonResponseDTO> updatePerson(
            @PathVariable Long id,
            @RequestBody PersonRequestDTO personRequestDTO) {
        PersonResponseDTO response = personService.updatePerson(id, personRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            summary = "Ativar uma pessoa",
            description = "Ativa uma pessoa inativa pelo seu identificador (id). Lança erro se a pessoa não existir ou já estiver ativa.",
            parameters = {
                    @Parameter(name = "id", description = "ID da pessoa", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pessoa ativada com sucesso",
                            content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Pessoa já está ativa", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content)
            }
    )
    @PutMapping("/{id}/activate")
    public ResponseEntity<PersonResponseDTO> activatePerson(@PathVariable Long id) {
        PersonResponseDTO activatedPerson = personService.activatePerson(id);
        return ResponseEntity.ok(activatedPerson);
    }
}
