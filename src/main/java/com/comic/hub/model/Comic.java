package com.comic.hub.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.comic.hub.model.converter.BooleanToIntegerConverter;

@Entity
@Table(name = "tb_comic")

public class Comic {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_comic")
	private Integer idComic;
	
	@Column(name = "titulo")
	private String titulo;
	
	@Column(name = "sinopsis")
	private String sinopsis;
	
    @ManyToOne
    @JoinColumn(name = "id_autor", nullable = false)
    private Autor autor;

	@ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
	private Categoria categoria;

	@Column(name = "ruta_imagen_portada", length = 1000)
	private String rutaImagenPortada;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private ComicEstado estado = ComicEstado.EN_EMISION;

    @Column(name = "activo", nullable = false)
    @Convert(converter = BooleanToIntegerConverter.class)
    private boolean activo = true;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "fecha_finalizacion")
    private LocalDate fechaFinalizacion;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;


	public Comic() {}


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


	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

    public String getRutaImagenPortada() {
        return rutaImagenPortada;
    }

    public void setRutaImagenPortada(String rutaImagenPortada) {
        this.rutaImagenPortada = rutaImagenPortada;
    }

    public ComicEstado getEstado() {
        return estado;
    }

    public void setEstado(ComicEstado estado) {
        this.estado = estado;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public LocalDate getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDate fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
