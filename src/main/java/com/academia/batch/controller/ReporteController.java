package com.academia.batch.controller;

// @RestController en /api/reportes que usa ReporteRepository: GET / lista todos los reportes; GET /estado/{estado} devuelve los que tengan ese estado (convertido a mayusculas) usando findByEstado.

import com.academia.batch.model.EstudianteReporte;
import com.academia.batch.repository.ReporteRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteRepository repository;

    public ReporteController(ReporteRepository repository) {
        this.repository = repository;
    }

    // GET /api/reportes
    @GetMapping("/api/reportes")
    public List<EstudianteReporte> listarReportes() {
        return repository.findAll();
    }

    // GET /api/reportes/estado/{estado}
    @GetMapping("/estado/{estado}")
    public List<EstudianteReporte> listarReportesPorEstado(@PathVariable String estado) {
        return repository.findByEstado(estado.toUpperCase());
    }
}
