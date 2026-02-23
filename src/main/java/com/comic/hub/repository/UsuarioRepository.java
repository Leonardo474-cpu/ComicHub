package com.comic.hub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comic.hub.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	
	Optional<Usuario> findByCorreo(String correo);
	// WAOS
}







