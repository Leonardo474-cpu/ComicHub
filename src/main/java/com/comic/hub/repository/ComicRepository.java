package com.comic.hub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.comic.hub.model.Comic;

public interface ComicRepository extends JpaRepository<Comic, Integer> {

    @Override
    @EntityGraph(attributePaths = {"autor", "categoria"})
    List<Comic> findAll();

    @Override
    @EntityGraph(attributePaths = {"autor", "categoria"})
    Page<Comic> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"autor", "categoria"})
    Page<Comic> findByActivo(boolean activo, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"autor", "categoria"})
    Optional<Comic> findById(Integer id);
}
