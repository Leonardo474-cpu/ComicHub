package com.comic.hub.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comic.hub.model.Plan;
import com.comic.hub.model.Suscripcion;
import com.comic.hub.model.Usuario;
import com.comic.hub.repository.PlanRepository;
import com.comic.hub.repository.SuscripcionRepository;
import com.comic.hub.repository.UsuarioRepository;
import com.comic.hub.service.SuscripcionService;

@Service
public class SuscripcionServiceImpl implements SuscripcionService {

    private static final String ESTADO_ACTIVA = "ACTIVA";
    private static final String ESTADO_FINALIZADA = "FINALIZADA";

    private final PlanRepository planRepository;
    private final SuscripcionRepository suscripcionRepository;
    private final UsuarioRepository usuarioRepository;

    public SuscripcionServiceImpl(PlanRepository planRepository,
                                  SuscripcionRepository suscripcionRepository,
                                  UsuarioRepository usuarioRepository) {
        this.planRepository = planRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Plan> listarPlanesActivos() {
        return planRepository.findByActivoTrueOrderByPrecioAsc();
    }

    @Override
    public Optional<Suscripcion> obtenerSuscripcionActiva(Integer idUsuario) {
        return suscripcionRepository.findFirstByUsuario_IdAndEstadoIgnoreCaseOrderByFechaFinDesc(idUsuario, ESTADO_ACTIVA);
    }

    @Override
    @Transactional
    public void contratarPlan(Integer idPlan, Usuario usuarioSesion) {
        if (usuarioSesion == null || usuarioSesion.getId() == null) {
            throw new RuntimeException("Debes iniciar sesion para seleccionar un plan");
        }

        Usuario usuario = usuarioRepository.findById(usuarioSesion.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("Tu cuenta esta inactiva. Contacta con soporte.");
        }

        Plan plan = planRepository.findById(idPlan)
                .orElseThrow(() -> new RuntimeException("El plan seleccionado no existe"));

        if (!plan.getActivo()) {
            throw new RuntimeException("El plan seleccionado no esta disponible");
        }

        LocalDate hoy = LocalDate.now();
        List<Suscripcion> suscripcionesActivas = suscripcionRepository
                .findByUsuario_IdAndEstadoIgnoreCaseOrderByFechaInicioDesc(usuario.getId(), ESTADO_ACTIVA);

        for (Suscripcion activa : suscripcionesActivas) {
            activa.setEstado(ESTADO_FINALIZADA);
            activa.setFechaFin(hoy.minusDays(1));
            activa.setObservacion("Plan reemplazado por una nueva suscripcion");
        }
        if (!suscripcionesActivas.isEmpty()) {
            suscripcionRepository.saveAll(suscripcionesActivas);
        }

        Suscripcion nueva = new Suscripcion();
        nueva.setUsuario(usuario);
        nueva.setPlan(plan);
        nueva.setEstado(ESTADO_ACTIVA);
        nueva.setFechaInicio(hoy);
        nueva.setFechaFin(hoy.plusDays(Math.max(plan.getDiasDuracion(), 1) - 1L));
        double precioPlan = plan.getPrecio() == null ? 0.0 : plan.getPrecio();
        nueva.setPrecioAplicado(BigDecimal.valueOf(precioPlan));
        nueva.setObservacion("Suscripcion creada desde ComicHub");

        suscripcionRepository.save(nueva);
    }

    @Override
    public Page<Suscripcion> listarSuscripciones(String estado, String q, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), size);
        String busqueda = q == null ? "" : q.trim();
        boolean tieneBusqueda = !busqueda.isBlank();

        if ("ACTIVA".equalsIgnoreCase(estado) || "FINALIZADA".equalsIgnoreCase(estado)) {
            return tieneBusqueda
                    ? suscripcionRepository.findByEstadoIgnoreCaseAndUsuario_NombreCompletoContainingIgnoreCase(
                    estado,
                    busqueda,
                    pageable
            )
                    : suscripcionRepository.findByEstadoIgnoreCase(estado, pageable);
        }

        return tieneBusqueda
                ? suscripcionRepository.findByUsuario_NombreCompletoContainingIgnoreCase(busqueda, pageable)
                : suscripcionRepository.findAll(pageable);
    }
}
