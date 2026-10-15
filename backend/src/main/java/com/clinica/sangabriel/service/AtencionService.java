package com.clinica.sangabriel.service;

import com.clinica.sangabriel.model.Atencion;
import com.clinica.sangabriel.model.Cita;
import com.clinica.sangabriel.repository.AtencionRepository;
import com.clinica.sangabriel.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AtencionService {

    @Autowired
    private AtencionRepository atencionRepository;

    @Autowired
    private CitaRepository citaRepository;

    public List<Atencion> listarTodas() {
        return atencionRepository.findAll();
    }

    public Atencion registrar(Atencion atencion) {
        if (atencion.getCita() != null && atencion.getCita().getIdCita() != null) {
            citaRepository.findById(atencion.getCita().getIdCita()).ifPresent(cita -> {
                cita.setEstado(Cita.EstadoCita.ATENDIDA);
                citaRepository.save(cita);
            });
        }
        return atencionRepository.save(atencion);
    }
}