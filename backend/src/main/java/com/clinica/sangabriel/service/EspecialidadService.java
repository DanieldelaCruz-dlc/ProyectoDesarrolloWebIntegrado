package com.clinica.sangabriel.service;

import com.clinica.sangabriel.model.Especialidad;
import com.clinica.sangabriel.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }

    public Especialidad registrar(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }
}