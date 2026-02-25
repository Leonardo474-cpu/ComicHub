package com.comic.hub.mapper;

import com.comic.hub.dto.request.UsuarioAdminRequestDto;
import com.comic.hub.dto.request.UsuarioRegistroRequestDto;
import com.comic.hub.dto.response.UsuarioListResponseDto;
import com.comic.hub.model.Rol;
import com.comic.hub.model.Usuario;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario toEntityForRegistro(UsuarioRegistroRequestDto dto, Rol rolCliente) {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setCorreo(dto.getCorreo());
        usuario.setPassword(dto.getPassword());
        usuario.setActivo(true);
        usuario.setRol(rolCliente);
        return usuario;
    }

    public static Usuario toEntityForAdmin(UsuarioAdminRequestDto dto, Rol rol, String passwordActual, boolean activoActual) {
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setCorreo(dto.getCorreo());
        usuario.setPassword(passwordActual);
        usuario.setActivo(activoActual);
        usuario.setRol(rol);
        return usuario;
    }

    public static UsuarioAdminRequestDto toAdminRequestDto(Usuario usuario) {
        UsuarioAdminRequestDto dto = new UsuarioAdminRequestDto();
        dto.setId(usuario.getId());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setCorreo(usuario.getCorreo());
        if (usuario.getRol() != null) {
            dto.setRolId(usuario.getRol().getCodRol());
        }
        return dto;
    }

    public static UsuarioListResponseDto toListResponseDto(Usuario usuario) {
        UsuarioListResponseDto dto = new UsuarioListResponseDto();
        dto.setId(usuario.getId());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setCorreo(usuario.getCorreo());
        dto.setActivo(usuario.getActivo());
        if (usuario.getRol() != null) {
            dto.setNombreRol(usuario.getRol().getNombreRol());
        }
        return dto;
    }
}
