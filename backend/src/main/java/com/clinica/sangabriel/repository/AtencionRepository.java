package com.clinica.sangabriel.repository;

import com.clinica.sangabriel.model.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Integer> {
}