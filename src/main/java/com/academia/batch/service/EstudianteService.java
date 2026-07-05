package com.academia.batch.service;

// @Service con inyeccion por constructor de EstudianteRepository.Metodo contarAprobados() que devuelve cuantos estudiantes tienen promedio >= 70,usando findAll() y un stream con filter y count.

import com.academia.batch.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

@Service
public class EstudianteService {

    private final EstudianteRepository repository;

    // Inyeccion por constructor (Spring Core)
    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    // Cuenta cuantos estudiantes tienen promedio aprobatorio (>= 70)
    public long contarAprobados() {
        return repository.findAll().stream()
                .filter(e -> e.getPromedio() >= 70)
                .count();
    }
}
