package com.clinica.sangabriel.controller;

import com.clinica.sangabriel.model.Especialidad;
import com.clinica.sangabriel.service.EspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@CrossOrigin(origins = "*") // Permite peticiones desde el frontend
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    // GET: http://localhost:8080/api/especialidades
    @GetMapping
    public ResponseEntity<List<Especialidad>> listar() {
        return ResponseEntity.ok(especialidadService.listarTodas());
    }

    // POST: http://localhost:8080/api/especialidades
    @PostMapping
    public ResponseEntity<Especialidad> crear(@RequestBody Especialidad especialidad) {
        Especialidad guardada = especialidadService.registrar(especialidad);
        return new ResponseEntity<>(guardada, HttpStatus.CREATED);
    }
}