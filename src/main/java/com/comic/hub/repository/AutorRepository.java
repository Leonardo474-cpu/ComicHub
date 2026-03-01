package com.comic.hub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.comic.hub.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Integer> {

    Optional<Autor> findByNombre(String nombre);

    Optional<Autor> findByNombreIgnoreCase(String nombre);

    Page<Autor> findByActivo(boolean activo, Pageable pageable);

    Page<Autor> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Autor> findByActivoAndNombreContainingIgnoreCase(boolean activo, String nombre, Pageable pageable);
}
