package com.comic.hub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comic.hub.model.Usuario;
import com.comic.hub.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    public List<Usuario> listarTodos() {
        return repo.findAll();
    }

    public Usuario guardar(Usuario usuario) {
        return repo.save(usuario);
    }

    public Usuario buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}
