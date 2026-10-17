package com.clinica.sangabriel.controller;

import com.clinica.sangabriel.model.Usuario;
import com.clinica.sangabriel.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository usuarios;

    public UsuarioController(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    // Nunca devolver Usuario completo: contiene el hash de la contrasena.
    public record PerfilResponse(Long id, String username, String rol, boolean activo) {}

    @GetMapping("/me")
    public ResponseEntity<PerfilResponse> miPerfil(Authentication autenticacion) {
        Usuario usuario = usuarios.findByUsername(autenticacion.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(new PerfilResponse(
                usuario.getId(), usuario.getUsername(), usuario.getRol().getNombre(),
                Boolean.TRUE.equals(usuario.getEstado())));
    }
}
