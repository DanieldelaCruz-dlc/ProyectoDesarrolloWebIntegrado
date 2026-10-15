package com.clinica.sangabriel.controller;

import com.clinica.sangabriel.model.Paciente;
import com.clinica.sangabriel.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<Paciente>> listar(@RequestParam(required = false) String dni) {
        if (dni != null && !dni.isBlank()) {
            return pacienteService.buscarPorDni(dni)
                    .map(paciente -> ResponseEntity.ok(List.of(paciente)))
                    .orElseGet(() -> ResponseEntity.ok(List.of()));
        }
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Paciente> crear(@RequestBody Paciente paciente) {
        return new ResponseEntity<>(pacienteService.registrar(paciente), HttpStatus.CREATED);
    }
}