package com.comic.hub.dto.request;

import com.comic.hub.model.ComicEstado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ComicAdminRequestDto {

    private Integer idComic;

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    private String sinopsis;

    @NotNull(message = "El autor es obligatorio")
    private Integer autorId;

    @NotNull(message = "La categoria es obligatoria")
    private Integer categoriaId;

    @NotNull(message = "El estado es obligatorio")
    private ComicEstado estado;

    private String rutaImagenPortada;

    public Integer getIdComic() {
        return idComic;
    }

    public void setIdComic(Integer idComic) {
        this.idComic = idComic;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Integer getAutorId() {
        return autorId;
    }

    public void setAutorId(Integer autorId) {
        this.autorId = autorId;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public ComicEstado getEstado() {
        return estado;
    }

    public void setEstado(ComicEstado estado) {
        this.estado = estado;
    }

    public String getRutaImagenPortada() {
        return rutaImagenPortada;
    }

    public void setRutaImagenPortada(String rutaImagenPortada) {
        this.rutaImagenPortada = rutaImagenPortada;
    }
}
