package com.comic.hub.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comic.hub.model.Suscripcion;
import com.comic.hub.repository.SuscripcionRepository;
import com.comic.hub.service.SuscripcionService;

@Service
public class SuscripcionServiceImpl implements SuscripcionService {

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    @Override
    public List<Suscripcion> listarSuscripciones() {
        return suscripcionRepository.findAll();
    }
}