package com.clinica.sangabriel.controller;

import com.clinica.sangabriel.dto.LoginRequest;
import com.clinica.sangabriel.dto.LoginResponse;
import com.clinica.sangabriel.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        UserDetails userDetails = User.withUsername(request.getUsername())
                .password("")
                .authorities("USER")
                .build();
        
        String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}