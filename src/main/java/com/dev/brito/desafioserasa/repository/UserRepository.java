package com.dev.brito.desafioserasa.repository;

import com.dev.brito.desafioserasa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
   
    UserDetails findByLogin(String login);
}