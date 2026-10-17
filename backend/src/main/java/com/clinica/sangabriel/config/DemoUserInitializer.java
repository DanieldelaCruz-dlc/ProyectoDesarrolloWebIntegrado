package com.clinica.sangabriel.config;

import com.clinica.sangabriel.model.Rol;
import com.clinica.sangabriel.model.Usuario;
import com.clinica.sangabriel.repository.RolRepository;
import com.clinica.sangabriel.repository.UsuarioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// Solo se ejecuta cuando DEMO_SEED_ENABLED=true, para pruebas locales.
@Component
@ConditionalOnProperty(name = "demo.seed.enabled", havingValue = "true")
public class DemoUserInitializer implements ApplicationRunner {
    private final RolRepository roles;
    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;

    @Value("${demo.seed.username:}")
    private String username;
    @Value("${demo.seed.password:}")
    private String password;

    public DemoUserInitializer(RolRepository roles, UsuarioRepository usuarios, PasswordEncoder encoder) {
        this.roles = roles;
        this.usuarios = usuarios;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments argumentos) {
        if (username.isBlank() || password.length() < 12) {
            throw new IllegalStateException(
                "Para pruebas, define DEMO_SEED_USERNAME y DEMO_SEED_PASSWORD (12+ caracteres)");
        }
        for (String nombre : new String[]{"ADMIN", "RECEPCION", "MEDICO"}) {
            roles.findByNombre(nombre).orElseGet(() -> {
                Rol rol = new Rol();
                rol.setNombre(nombre);
                return roles.save(rol);
            });
        }
        if (!usuarios.existsByUsername(username)) {
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(encoder.encode(password));
            usuario.setEstado(true);
            usuario.setRol(roles.findByNombre("RECEPCION").orElseThrow());
            usuarios.save(usuario);
        }
    }
}
