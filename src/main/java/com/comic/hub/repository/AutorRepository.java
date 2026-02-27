package com.comic.hub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comic.hub.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Integer> {

    Optional<Autor> findByNombre(String nombre);

    Optional<Autor> findByNombreIgnoreCase(String nombre);
}
