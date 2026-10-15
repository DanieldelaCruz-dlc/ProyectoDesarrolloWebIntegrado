package com.clinica.sangabriel.repository;

import com.clinica.sangabriel.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
    // Métodos CRUD automáticos listos para usar
}