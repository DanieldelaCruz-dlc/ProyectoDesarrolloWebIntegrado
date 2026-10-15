package com.clinica.sangabriel.service;

import com.clinica.sangabriel.model.Medico;
import com.clinica.sangabriel.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    public List<Medico> listarPorEspecialidad(Integer idEspecialidad) {
        return medicoRepository.findByEspecialidadIdEspecialidad(idEspecialidad);
    }

    public Medico registrar(Medico medico) {
        return medicoRepository.save(medico);
    }
}