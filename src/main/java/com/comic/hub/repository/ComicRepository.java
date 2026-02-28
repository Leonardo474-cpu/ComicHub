package com.comic.hub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
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

    @EntityGraph(attributePaths = {"autor", "categoria"})
    Page<Comic> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

    @EntityGraph(attributePaths = {"autor", "categoria"})
    Page<Comic> findByActivoAndTituloContainingIgnoreCase(boolean activo, String titulo, Pageable pageable);

    @EntityGraph(attributePaths = {"autor", "categoria"})
    @Query("""
            SELECT c
            FROM Comic c
            WHERE (:activo IS NULL OR c.activo = :activo)
              AND (:titulo = '' OR LOWER(c.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')))
              AND (:autorId IS NULL OR c.autor.idAutor = :autorId)
              AND (:categoriaId IS NULL OR c.categoria.idCategoria = :categoriaId)
            """)
    Page<Comic> buscarConFiltros(@Param("activo") Boolean activo,
                                 @Param("titulo") String titulo,
                                 @Param("autorId") Integer autorId,
                                 @Param("categoriaId") Integer categoriaId,
                                 Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"autor", "categoria"})
    Optional<Comic> findById(Integer id);
}
