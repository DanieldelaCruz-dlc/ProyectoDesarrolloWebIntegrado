package com.clinica.sangabriel.controller;

import com.clinica.sangabriel.dto.CitaRequest;
import com.clinica.sangabriel.dto.CitaResponse;
import com.clinica.sangabriel.model.Cita;
import com.clinica.sangabriel.service.CitaService;
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

    private final CitaService citaService;

        public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    // GET /api/citas y GET /api/citas?fecha=2026-11-20
     @GetMapping


    public ResponseEntity<List<CitaResponse>> listar(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
         if (fecha != null) {
             return ResponseEntity.ok(citaService.listarPorFecha(fecha));
         }
         return ResponseEntity.ok(citaService.listarTodas());
     }
 
    // POST /api/citas: se envian IDs, no objetos Paciente/Medico completos.
     @PostMapping

    public ResponseEntity<CitaResponse> crear(@RequestBody CitaRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(citaService.registrar(datos));
     }
 
    // PATCH /api/citas/1/estado?nuevoEstado=CANCELADA
     @PatchMapping("/{id}/estado")

    public ResponseEntity<CitaResponse> actualizarEstado(
             @PathVariable Integer id,
             @RequestParam Cita.EstadoCita nuevoEstado) {
         return citaService.actualizarEstado(id, nuevoEstado)
                 .map(ResponseEntity::ok)
                 .orElseGet(() -> ResponseEntity.notFound().build());
     }
}