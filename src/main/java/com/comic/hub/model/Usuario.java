package com.comic.hub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; 

@Getter
@Setter
@NoArgsConstructor
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

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    @NotNull(message = "Debe seleccionar un rol")
    private Rol rol;


}
