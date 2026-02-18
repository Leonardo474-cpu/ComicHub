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
    
    
    
    
    
    
    
    
    
 // =========================
    // 🔥 MÉTODO AGREGADO PARA LOGIN 
    // =========================
    public Usuario login(String correo, String password) {

        // Busca usuario por correo
        Usuario usuario = repo.findByCorreo(correo) //el repo.find viene del   @Autowired que le asigna el nombre de repo por eso no va UsuarioRepository
                .orElseThrow(() -> new RuntimeException("Correo incorrecto"));

        // Valida contraseña
        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Retorna usuario si todo está correcto
        return usuario;
    }
    
    
    
}
