package com.dev.brito.desafioserasa.repository;

import com.dev.brito.desafioserasa.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.nio.channels.FileChannel;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    Optional<Person> findByIdAndActiveTrue(Long id);
}
