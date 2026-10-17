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
@CrossOrigin(origins = "*")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<Especialidad>> listar() {
        return ResponseEntity.ok(especialidadService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Especialidad> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(especialidadService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Especialidad> crear(@RequestBody Especialidad especialidad) {
        return new ResponseEntity<>(especialidadService.registrar(especialidad), HttpStatus.CREATED);
    }
}
