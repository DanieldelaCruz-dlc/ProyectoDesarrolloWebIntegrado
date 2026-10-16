package com.clinica.sangabriel.service;

import com.clinica.sangabriel.dto.CitaRequest;
import com.clinica.sangabriel.dto.CitaResponse;
import com.clinica.sangabriel.model.Cita;
import com.clinica.sangabriel.model.Medico;
import com.clinica.sangabriel.model.Paciente;
import com.clinica.sangabriel.repository.CitaRepository;
import com.clinica.sangabriel.repository.MedicoRepository;
import com.clinica.sangabriel.repository.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
 
    public CitaService(CitaRepository citaRepository,
                       PacienteRepository pacienteRepository,
                       MedicoRepository medicoRepository) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
     }
 

    @Transactional(readOnly = true)
    public List<CitaResponse> listarTodas() {
        return citaRepository.findAll().stream()
                .map(CitaResponse::desde)
                .toList();
     }
 

    @Transactional(readOnly = true)
    public List<CitaResponse> listarPorFecha(LocalDate fecha) {
        return citaRepository.findByFechaCita(fecha).stream()
                .map(CitaResponse::desde)
                .toList();
     }
 

    @Transactional
    public CitaResponse registrar(CitaRequest datos) {
        if (datos == null || datos.idPaciente() == null || datos.idMedico() == null
                || datos.fechaCita() == null || datos.horaCita() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Faltan paciente, medico, fecha u hora");
        }
        if (datos.idPaciente() <= 0 || datos.idMedico() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Los identificadores deben ser positivos");
        }
        if (datos.fechaCita().atTime(datos.horaCita()).isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se pueden reservar citas en el pasado");
        }
        String motivo = datos.motivoConsulta() == null ? null : datos.motivoConsulta().trim();
        if (motivo != null && motivo.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El motivo no debe exceder 255 caracteres");
        }

        Paciente paciente = pacienteRepository.findById(datos.idPaciente())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El paciente no existe"));
        Medico medico = medicoRepository.findById(datos.idMedico())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El medico no existe"));
        if (Boolean.FALSE.equals(medico.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede reservar con un medico inactivo");
        }

        if (existeChoque(datos.idMedico(), datos.fechaCita(), datos.horaCita())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El medico ya tiene una cita a esa hora");
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFechaCita(datos.fechaCita());
        cita.setHoraCita(datos.horaCita());
        cita.setMotivoConsulta(motivo);
        cita.setEstado(Cita.EstadoCita.PENDIENTE);
        return CitaResponse.desde(citaRepository.save(cita));
    }

    
    @Transactional
    public Optional<CitaResponse> actualizarEstado(Integer idCita, Cita.EstadoCita nuevoEstado) {
        if (nuevoEstado == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes indicar el nuevo estado");
        }
         return citaRepository.findById(idCita).map(cita -> {
            // Si se intenta reactivar una cita cancelada, comprobar la disponibilidad.
            if (cita.getEstado() == Cita.EstadoCita.CANCELADA
                    && nuevoEstado != Cita.EstadoCita.CANCELADA) {
                if (cita.getFechaCita().atTime(cita.getHoraCita()).isBefore(LocalDateTime.now())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "No se puede reactivar una cita pasada");
                }
                if (existeChoque(cita.getMedico().getIdMedico(),
                        cita.getFechaCita(), cita.getHoraCita())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Ya existe una reserva activa en ese horario");
                }
            }
             cita.setEstado(nuevoEstado);

            return CitaResponse.desde(citaRepository.save(cita));
         });
     }

    private boolean existeChoque(Integer idMedico, LocalDate fecha, java.time.LocalTime hora) {
        return citaRepository.existsByMedico_IdMedicoAndFechaCitaAndHoraCitaAndEstadoNot(
                idMedico, fecha, hora, Cita.EstadoCita.CANCELADA);
    }
}
