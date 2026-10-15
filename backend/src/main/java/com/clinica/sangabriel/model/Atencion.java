package com.clinica.sangabriel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "atenciones")
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atencion")
    private Integer idAtencion;

    @OneToOne
    @JoinColumn(name = "id_cita", nullable = false, unique = true)
    private Cita cita;

    @Column(name = "fecha_hora_atencion", insertable = false, updatable = false)
    private LocalDateTime fechaHoraAtencion;

    @Column(name = "signos_vitales", length = 255)
    private String signosVitales;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "tratamiento_receta", columnDefinition = "TEXT")
    private String tratamientoReceta;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    public Atencion() {
    }

    public Integer getIdAtencion() {
        return idAtencion;
    }

    public void setIdAtencion(Integer idAtencion) {
        this.idAtencion = idAtencion;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public LocalDateTime getFechaHoraAtencion() {
        return fechaHoraAtencion;
    }

    public void setFechaHoraAtencion(LocalDateTime fechaHoraAtencion) {
        this.fechaHoraAtencion = fechaHoraAtencion;
    }

    public String getSignosVitales() {
        return signosVitales;
    }

    public void setSignosVitales(String signosVitales) {
        this.signosVitales = signosVitales;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamientoReceta() {
        return tratamientoReceta;
    }

    public void setTratamientoReceta(String tratamientoReceta) {
        this.tratamientoReceta = tratamientoReceta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}