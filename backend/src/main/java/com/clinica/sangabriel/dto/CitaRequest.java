package com.clinica.sangabriel.dto;

import java.time.LocalDate;
import java.time.LocalTime;

// Datos que Postman debe enviar para crear una cita.
// Solo recibimos IDs: no permitimos crear pacientes/medicos desde este endpoint.
public record CitaRequest(
        Integer idPaciente,
        Integer idMedico,
        LocalDate fechaCita,
        LocalTime horaCita,
        String motivoConsulta) {
}