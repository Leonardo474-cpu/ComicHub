package com.comic.hub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioAdminRequestDto {

    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreCompleto;

    @Email(message = "Correo invalido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotNull(message = "El rol es obligatorio")
    private Integer rolId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }
}
