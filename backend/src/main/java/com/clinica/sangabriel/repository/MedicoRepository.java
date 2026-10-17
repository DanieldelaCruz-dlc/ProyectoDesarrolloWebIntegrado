package com.clinica.sangabriel.repository;

import com.clinica.sangabriel.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    List<Medico> findByEspecialidadIdEspecialidad(Integer idEspecialidad);

    boolean existsByDni(String dni);

    boolean existsByCmp(String cmp);
}
