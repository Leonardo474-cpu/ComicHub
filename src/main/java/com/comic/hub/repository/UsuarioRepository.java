package com.comic.hub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.comic.hub.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	@Override
	@EntityGraph(attributePaths = "rol")
	List<Usuario> findAll();

	@Override
	@EntityGraph(attributePaths = "rol")
	Page<Usuario> findAll(Pageable pageable);

	@EntityGraph(attributePaths = "rol")
	Page<Usuario> findByActivo(boolean activo, Pageable pageable);

	@EntityGraph(attributePaths = "rol")
	Optional<Usuario> findByCorreo(String correo);
}



