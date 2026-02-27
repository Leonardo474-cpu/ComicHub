package com.comic.hub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comic.hub.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByDescripcion(String descripcion);

    Optional<Categoria> findByDescripcionIgnoreCase(String descripcion);
}
