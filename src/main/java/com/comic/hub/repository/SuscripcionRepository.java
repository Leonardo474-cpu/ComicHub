package com.comic.hub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comic.hub.model.Suscripcion;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Integer> {

    List<Suscripcion> findByUsuario_IdAndEstadoIgnoreCaseOrderByFechaInicioDesc(Integer idUsuario, String estado);

    Optional<Suscripcion> findFirstByUsuario_IdAndEstadoIgnoreCaseOrderByFechaFinDesc(Integer idUsuario, String estado);
}
