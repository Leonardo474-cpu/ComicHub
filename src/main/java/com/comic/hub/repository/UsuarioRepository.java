package com.comic.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comic.hub.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	// WAOS
}
