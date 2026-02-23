package com.comic.hub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comic.hub.model.Rol;
import com.comic.hub.model.Usuario;
import com.comic.hub.repository.RolRepository;
import com.comic.hub.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;
    
    @Autowired
    private RolRepository rolRepo;

    public List<Usuario> listarTodos() {
        return repo.findAll();
    }

    public Usuario guardar(Usuario usuario) {
        if (usuario.getId() != null) {
            Usuario existente = repo.findById(usuario.getId()).orElse(null);

            usuario.setPassword(existente.getPassword());
        }
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
        Usuario usuario = repo.findByCorreo(correo) 
                .orElseThrow(() -> new RuntimeException("Correo incorrecto"));

        // Valida contraseña
        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Retorna usuario si todo está correcto
        return usuario;
    }
    
    
    // ✅ REGISTRO (PUBLICO)
    public void registrar(Usuario usuario) {
        Rol rolCliente = rolRepo.findByNombreRol("CLIENTE");
        usuario.setRol(rolCliente);

        repo.save(usuario);
    }
    
    
}
