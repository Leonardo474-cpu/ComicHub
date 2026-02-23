package com.comic.hub.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_comic")

@Getter
@Setter
@NoArgsConstructor
public class Comic {

	
	@Id
	@GeneratedValue(strategy =   GenerationType.IDENTITY)
	@Column(name = "id_comic")
	private Integer idComic;
	
	@Column(name = "titulo")
	private String titulo;
	
	@Column(name = "sinopsis")
	private String sinopsis;
	
	@Column(name = "autor")
	private String autor;
	
	@Column(name = "ruta_imagen")
	private String  rutaImagen;
	
	@Column(name = "ruta_archivo")
	private String rutaArchivo;
	
	
	@ManyToOne
    @JoinColumn(name = "id_categoria")
	private Categoria Categoria;


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


	public String getAutor() {
		return autor;
	}


	public void setAutor(String autor) {
		this.autor = autor;
	}


	public String getRutaImagen() {
		return rutaImagen;
	}


	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}


	public String getRutaArchivo() {
		return rutaArchivo;
	}


	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}


	public Categoria getCategoria() {
		return Categoria;
	}


	public void setCategoria(Categoria categoria) {
		Categoria = categoria;
	}
	
}

