package com.clinica.sangabriel.repository;

import com.clinica.sangabriel.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByFechaCita(LocalDate fechaCita);
}