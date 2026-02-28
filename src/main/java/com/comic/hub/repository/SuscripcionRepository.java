package com.comic.hub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.comic.hub.model.Suscripcion;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Integer> {

    List<Suscripcion> findByUsuario_IdAndEstadoIgnoreCaseOrderByFechaInicioDesc(Integer idUsuario, String estado);

    Optional<Suscripcion> findFirstByUsuario_IdAndEstadoIgnoreCaseOrderByFechaFinDesc(Integer idUsuario, String estado);

    @Override
    @EntityGraph(attributePaths = {"usuario", "plan"})
    List<Suscripcion> findAll();

    @Override
    @EntityGraph(attributePaths = {"usuario", "plan"})
    Page<Suscripcion> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"usuario", "plan"})
    Page<Suscripcion> findByEstadoIgnoreCase(String estado, Pageable pageable);

    @EntityGraph(attributePaths = {"usuario", "plan"})
    Page<Suscripcion> findByUsuario_NombreCompletoContainingIgnoreCase(String nombreCompleto, Pageable pageable);

    @EntityGraph(attributePaths = {"usuario", "plan"})
    Page<Suscripcion> findByEstadoIgnoreCaseAndUsuario_NombreCompletoContainingIgnoreCase(String estado,
                                                                                           String nombreCompleto,
                                                                                           Pageable pageable);
}
