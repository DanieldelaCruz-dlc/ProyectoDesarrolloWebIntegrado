package com.clinica.sangabriel.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medicos")
@Data
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Integer idMedico;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 20)
    private String cmp;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    private String correo;

    @ManyToOne
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad especialidad;

    @Column(columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean estado = true;
}