package com.clinica.sangabriel.service;

import com.clinica.sangabriel.model.Cita;
import com.clinica.sangabriel.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    public List<Cita> listarPorFecha(LocalDate fecha) {
        return citaRepository.findByFechaCita(fecha);
    }

    public Cita registrar(Cita cita) {
        return citaRepository.save(cita);
    }

    public Optional<Cita> actualizarEstado(Integer idCita, Cita.EstadoCita nuevoEstado) {
        return citaRepository.findById(idCita).map(cita -> {
            cita.setEstado(nuevoEstado);
            return citaRepository.save(cita);
        });
    }
}