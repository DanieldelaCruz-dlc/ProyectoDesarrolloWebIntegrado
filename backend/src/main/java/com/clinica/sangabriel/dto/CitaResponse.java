package com.clinica.sangabriel.dto;

import com.clinica.sangabriel.model.Cita;
import java.time.LocalDate;
import java.time.LocalTime;

// Devuelve unicamente los datos necesarios para presentar la cita.
// No expone DNI, domicilio ni otros datos personales de paciente/medico.
public record CitaResponse(
        Integer idCita,
        Integer idPaciente,
        Integer idMedico,
        LocalDate fechaCita,
        LocalTime horaCita,
        String motivoConsulta,
        Cita.EstadoCita estado) {

    public static CitaResponse desde(Cita cita) {
        return new CitaResponse(
                cita.getIdCita(),
                cita.getPaciente().getIdPaciente(),
                cita.getMedico().getIdMedico(),
                cita.getFechaCita(),
                cita.getHoraCita(),
                cita.getMotivoConsulta(),
                cita.getEstado());
    }
}