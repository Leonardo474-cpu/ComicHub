package com.comic.hub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import com.comic.hub.model.converter.BooleanToIntegerConverter;

@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Email(message = "Correo inválido")
    @NotBlank(message = "El correo es obligatorio")
    @Column(name = "correo", unique = true)
    private String correo;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "activo", nullable = false)
    @Convert(converter = BooleanToIntegerConverter.class)
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(name = "fecha_registro", insertable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    public Usuario() {

    }

	public Integer getId() {
		return id;

	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public String getCorreo() {
		return correo;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public Rol getRol() {
		return rol;
	}
	
	public boolean getActivo() {
		return activo;
	}

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
	
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
