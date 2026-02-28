package com.comic.hub.service;

import java.util.List;
import java.util.Optional;

import com.comic.hub.model.Plan;
import com.comic.hub.model.Suscripcion;
import com.comic.hub.model.Usuario;

public interface SuscripcionService {

    List<Plan> listarPlanesActivos();

    Optional<Suscripcion> obtenerSuscripcionActiva(Integer idUsuario);

    void contratarPlan(Integer idPlan, Usuario usuarioSesion);

    List<Suscripcion> listarSuscripciones();
}
