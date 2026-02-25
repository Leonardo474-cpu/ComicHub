package com.comic.hub.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.comic.hub.dto.request.UsuarioAdminRequestDto;
import com.comic.hub.dto.request.UsuarioRegistroRequestDto;
import com.comic.hub.dto.response.UsuarioListResponseDto;
import com.comic.hub.mapper.UsuarioMapper;
import com.comic.hub.model.Rol;
import com.comic.hub.model.Usuario;
import com.comic.hub.repository.RolRepository;
import com.comic.hub.repository.UsuarioRepository;
import com.comic.hub.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    public List<UsuarioListResponseDto> listarTodos() {
        return usuarioRepository.findAll().stream().map(UsuarioMapper::toListResponseDto).toList();
    }

    @Override
    public UsuarioAdminRequestDto buscarPorIdParaEdicion(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return UsuarioMapper.toAdminRequestDto(usuario);
    }

    @Override
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario login(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo incorrecto"));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }

    @Override
    public void registrar(UsuarioRegistroRequestDto usuarioRegistroRequestDto) {
        Rol rolCliente = rolRepository.findByNombreRol("CLIENTE");
        if (rolCliente == null) {
            throw new RuntimeException("Rol CLIENTE no encontrado");
        }
        Usuario usuario = UsuarioMapper.toEntityForRegistro(usuarioRegistroRequestDto, rolCliente);
        usuarioRepository.save(usuario);
    }

    @Override
    public void guardarDesdeAdmin(UsuarioAdminRequestDto usuarioAdminRequestDto) {
        Usuario existente = usuarioRepository.findById(usuarioAdminRequestDto.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Rol rol = rolRepository.findById(usuarioAdminRequestDto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuarioActualizado = UsuarioMapper.toEntityForAdmin(
                usuarioAdminRequestDto,
                rol,
                existente.getPassword(),
                existente.getActivo());

        usuarioRepository.save(usuarioActualizado);
    }
}
