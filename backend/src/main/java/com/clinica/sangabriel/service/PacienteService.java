package com.clinica.sangabriel.service;

import com.clinica.sangabriel.model.Paciente;
import com.clinica.sangabriel.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> buscarPorDni(String dni) {
        return pacienteRepository.findByDni(dni);
    }

    public Paciente registrar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }
}