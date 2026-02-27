package com.comic.hub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.comic.hub.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    Optional<Categoria> findByDescripcion(String descripcion);

    Optional<Categoria> findByDescripcionIgnoreCase(String descripcion);

    Page<Categoria> findByActivo(boolean activo, Pageable pageable);
}
