package com.comic.hub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.comic.hub.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	@Override
	@EntityGraph(attributePaths = "rol")
	List<Usuario> findAll();

	@EntityGraph(attributePaths = "rol")
	Optional<Usuario> findByCorreo(String correo);
}




