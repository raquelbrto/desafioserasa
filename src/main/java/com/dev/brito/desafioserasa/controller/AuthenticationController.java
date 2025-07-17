package com.dev.brito.desafioserasa.controller;

import com.dev.brito.desafioserasa.config.security.TokenService;
import com.dev.brito.desafioserasa.dto.AuthenticationDTO;
import com.dev.brito.desafioserasa.dto.LoginResponseDTO;
import com.dev.brito.desafioserasa.dto.RegisterDTO;
import com.dev.brito.desafioserasa.entity.User;
import com.dev.brito.desafioserasa.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {
    private static final Log logger = LogFactory.getLog(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;

    private final UserRepository repository;

    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository repository, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.tokenService = tokenService;
    }

    @Operation(
            summary = "Autenticar usuário (login)",
            description = "Autentica um usuário e retorna um token JWT.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso, retorna o token JWT",
                            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.login(), authenticationDTO.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            logger.error("Error while authenticating user", e);
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Registrar novo usuário",
            description = "Registra um novo usuário no sistema. Apenas administradores e usuários autenticados podem registrar novos usuários.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Usuário já existe ou requisição inválida", content = @Content)
            }
    )
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO) {
        if (this.repository.findByLogin(registerDTO.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
        User newUser = new User(registerDTO.login(), encryptedPassword, registerDTO.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
