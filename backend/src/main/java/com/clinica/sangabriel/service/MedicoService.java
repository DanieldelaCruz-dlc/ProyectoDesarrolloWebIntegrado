package com.clinica.sangabriel.service;

import com.clinica.sangabriel.model.Especialidad;
import com.clinica.sangabriel.model.Medico;
import com.clinica.sangabriel.repository.EspecialidadRepository;
import com.clinica.sangabriel.repository.MedicoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;

    public MedicoService(MedicoRepository medicoRepository,
                         EspecialidadRepository especialidadRepository) {
        this.medicoRepository = medicoRepository;
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional(readOnly = true)
    public List<Medico> listarTodos() {
        return medicoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Medico> listarPorEspecialidad(Integer idEspecialidad) {
        return medicoRepository.findByEspecialidadIdEspecialidad(idEspecialidad);
    }

    @Transactional(readOnly = true)
    public Medico buscarPorId(Integer id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "El medico no existe"));
    }

    @Transactional
    public Medico registrar(Medico medico) {
        if (medico == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faltan los datos del medico");
        }
        String dni = requerido(medico.getDni(), "DNI", 8);
        String nombres = requerido(medico.getNombres(), "Nombres", 100);
        String apellidos = requerido(medico.getApellidos(), "Apellidos", 100);
        String cmp = requerido(medico.getCmp(), "CMP", 20);
        String telefono = opcional(medico.getTelefono(), "Telefono", 15);
        String correo = opcional(medico.getCorreo(), "Correo", 100);

        if (!dni.matches("\\d{8}")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El DNI debe tener exactamente 8 digitos");
        }
        if (correo != null && !correo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo no es valido");
        }
        if (medico.getEspecialidad() == null || medico.getEspecialidad().getIdEspecialidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debes indicar la especialidad (especialidad.idEspecialidad)");
        }

        Especialidad especialidad = especialidadRepository
                .findById(medico.getEspecialidad().getIdEspecialidad())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "La especialidad no existe"));
        if (Boolean.FALSE.equals(especialidad.getEstado())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La especialidad esta inactiva");
        }
        if (medicoRepository.existsByDni(dni)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un medico con ese DNI");
        }
        if (medicoRepository.existsByCmp(cmp)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un medico con ese CMP");
        }

        medico.setIdMedico(null); // siempre se crea uno nuevo
        medico.setDni(dni);
        medico.setNombres(nombres);
        medico.setApellidos(apellidos);
        medico.setCmp(cmp);
        medico.setTelefono(telefono);
        medico.setCorreo(correo);
        medico.setEspecialidad(especialidad);
        if (medico.getEstado() == null) {
            medico.setEstado(true);
        }
        try {
            return medicoRepository.save(medico);
        } catch (DataIntegrityViolationException ex) {
            // Respaldo ante dos registros simultaneos con el mismo DNI/CMP
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El DNI o el CMP ya estan registrados");
        }
    }

    private String requerido(String valor, String campo, int max) {
        if (valor == null || valor.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    campo + " es obligatorio");
        }
        return opcional(valor, campo, max);
    }

    private String opcional(String valor, String campo, int max) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String limpio = valor.trim();
        if (limpio.length() > max) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    campo + " no debe exceder " + max + " caracteres");
        }
        return limpio;
    }
}
