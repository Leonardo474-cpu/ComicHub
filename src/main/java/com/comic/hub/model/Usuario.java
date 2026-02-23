package com.comic.hub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; 

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

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    @NotNull(message = "Debe seleccionar un rol")
    private Rol rol;
    
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

	public String getPassword() {
		return password;
	}

	public Rol getRol() {
		return rol;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
    
}
