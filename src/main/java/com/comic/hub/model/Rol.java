package com.comic.hub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.comic.hub.model.converter.BooleanToIntegerConverter;

@Entity
@Table(name = "tb_rol")
public class Rol {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rol")
	private int codRol;
	
	@Column(name = "nombre")
	private String nombreRol;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activo", nullable = false)
    @Convert(converter = BooleanToIntegerConverter.class)
    private boolean activo = true;

	public Rol() {}

	public int getCodRol() {
		return codRol;
	}

	public void setCodRol(int codRol) {
		this.codRol = codRol;
	}

	public String getNombreRol() {
		return nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}
