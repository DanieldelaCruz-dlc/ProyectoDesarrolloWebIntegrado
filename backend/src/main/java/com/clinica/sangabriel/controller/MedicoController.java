package com.clinica.sangabriel.controller;

import com.clinica.sangabriel.model.Medico;
import com.clinica.sangabriel.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "*")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<Medico>> listar(@RequestParam(required = false) Integer idEspecialidad) {
        if (idEspecialidad != null) {
            return ResponseEntity.ok(medicoService.listarPorEspecialidad(idEspecialidad));
        }
        return ResponseEntity.ok(medicoService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Medico> crear(@RequestBody Medico medico) {
        return new ResponseEntity<>(medicoService.registrar(medico), HttpStatus.CREATED);
    }
}
