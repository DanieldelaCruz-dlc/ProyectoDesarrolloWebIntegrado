package com.clinica.sangabriel.controller;

import com.clinica.sangabriel.model.Cita;
import com.clinica.sangabriel.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping
    public ResponseEntity<List<Cita>> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (fecha != null) {
            return ResponseEntity.ok(citaService.listarPorFecha(fecha));
        }
        return ResponseEntity.ok(citaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<Cita> crear(@RequestBody Cita cita) {
        return new ResponseEntity<>(citaService.registrar(cita), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Cita> actualizarEstado(
            @PathVariable Integer id,
            @RequestParam Cita.EstadoCita nuevoEstado) {
        return citaService.actualizarEstado(id, nuevoEstado)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}