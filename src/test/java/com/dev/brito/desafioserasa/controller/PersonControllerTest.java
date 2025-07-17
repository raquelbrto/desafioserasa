package com.dev.brito.desafioserasa.controller;

import com.dev.brito.desafioserasa.config.security.TokenService;
import com.dev.brito.desafioserasa.dto.PersonRequestDTO;
import com.dev.brito.desafioserasa.dto.PersonResponseDTO;
import com.dev.brito.desafioserasa.repository.UserRepository;
import com.dev.brito.desafioserasa.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TokenService tokenService;

    private PersonRequestDTO sampleRequest;
    private PersonResponseDTO sampleResponse;

    @BeforeEach
    void setup() {
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
                "Raqurl",
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
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreatePersonSuccessfully() throws Exception {
        Mockito.when(personService.createPerson(any(PersonRequestDTO.class))).thenReturn(sampleResponse);

        String jsonRequest = """
                {
                  "name": "Raqurl",
                  "email": "raquel.aves@gmail.com",
                  "phone": "84994885502",
                  "age": 27,
                  "zipCode": "59215000",
                  "score": 1000,
                  "active": true
                }
                """;

        mockMvc.perform(post("/api/v1/persons")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void shouldReturnAllPersons() throws Exception {
        List<PersonResponseDTO> list = Arrays.asList(sampleResponse, sampleResponse);
        Mockito.when(personService.getAllPersons()).thenReturn(list);

        mockMvc.perform(get("/api/v1/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void shouldReturnPersonById() throws Exception {
        Mockito.when(personService.getPersonById(1L)).thenReturn(Optional.of(sampleResponse));

        mockMvc.perform(get("/api/v1/persons/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenPersonNotFound() throws Exception {
        Mockito.when(personService.getPersonById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/persons/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeletePerson() throws Exception {
        mockMvc.perform(delete("/api/v1/persons/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCannotDeletePerson() throws Exception {
        mockMvc.perform(delete("/api/v1/persons/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdatePersonSuccessfully() throws Exception {
        Mockito.when(personService.updatePerson(eq(1L), any(PersonRequestDTO.class))).thenReturn(sampleResponse);

        String jsonRequest = """
                {
                  "name": "Raquel",
                  "email": "raquel.aves@gmail.com",
                  "phone": "84994885502",
                  "age": 27,
                  "zipCode": "59215000",
                  "score": 1000
                }
                """;

        mockMvc.perform(put("/api/v1/persons/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void shouldReturnPagedFilteredPersons() throws Exception {
        Mockito.when(personService.filterPersons(any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleResponse)));

        mockMvc.perform(get("/api/v1/persons/search-filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }
}
