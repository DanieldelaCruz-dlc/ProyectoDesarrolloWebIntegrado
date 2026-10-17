package com.clinica.sangabriel.service;

import com.clinica.sangabriel.model.Especialidad;
import com.clinica.sangabriel.repository.EspecialidadRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional(readOnly = true)
    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Especialidad buscarPorId(Integer id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "La especialidad no existe"));
    }

    @Transactional
    public Especialidad registrar(Especialidad especialidad) {
        if (especialidad == null || especialidad.getNombre() == null
                || especialidad.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El nombre de la especialidad es obligatorio");
        }
        String nombre = especialidad.getNombre().trim();
        if (nombre.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El nombre no debe exceder 100 caracteres");
        }
        String descripcion = especialidad.getDescripcion() == null
                ? null : especialidad.getDescripcion().trim();
        if (descripcion != null && descripcion.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La descripcion no debe exceder 255 caracteres");
        }
        if (especialidadRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una especialidad con ese nombre");
        }

        especialidad.setIdEspecialidad(null); // siempre se crea una nueva
        especialidad.setNombre(nombre);
        especialidad.setDescripcion(descripcion);
        if (especialidad.getEstado() == null) {
            especialidad.setEstado(true);
        }
        try {
            return especialidadRepository.save(especialidad);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una especialidad con ese nombre");
        }
    }
}
