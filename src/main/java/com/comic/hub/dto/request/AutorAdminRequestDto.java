package com.comic.hub.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AutorAdminRequestDto {

    private Integer idAutor;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String seudonimo;

    public Integer getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Integer idAutor) {
        this.idAutor = idAutor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSeudonimo() {
        return seudonimo;
    }

    public void setSeudonimo(String seudonimo) {
        this.seudonimo = seudonimo;
    }
}
