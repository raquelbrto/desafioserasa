package com.dev.brito.desafioserasa.dto;

import com.dev.brito.desafioserasa.entity.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
