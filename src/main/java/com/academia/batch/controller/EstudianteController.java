package com.academia.batch.controller;

import com.academia.batch.repository.EstudianteEntity;
import com.academia.batch.repository.EstudianteRepository;
import com.academia.batch.service.EstudianteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteRepository estudianteRepository, EstudianteService estudianteService) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public ResponseEntity<List<EstudianteEntity>> listarTodos() {
        return ResponseEntity.ok(estudianteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteEntity> obtenerPorId(@PathVariable Long id) {
        return estudianteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/aprobados/total")
    public ResponseEntity<Map<String, Long>> totalAprobados() {
        return ResponseEntity.ok(Map.of("totalAprobados", estudianteService.contarAprobados()));
    }

    @PostMapping
    public ResponseEntity<EstudianteEntity> crear(@RequestBody EstudianteEntity estudiante) {
        EstudianteEntity creado = estudianteRepository.save(estudiante);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteEntity> reemplazar(@PathVariable Long id, @RequestBody EstudianteEntity estudiante) {
        return estudianteRepository.findById(id)
                .map(existente -> {
                    estudiante.setId(id);
                    EstudianteEntity actualizado = estudianteRepository.save(estudiante);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EstudianteEntity> actualizarGrupo(@PathVariable Long id, @RequestBody Map<String, String> cambios) {
        return estudianteRepository.findById(id)
                .map(estudiante -> {
                    if (cambios.containsKey("grupo")) {
                        estudiante.setGrupo(cambios.get("grupo"));
                    }
                    EstudianteEntity actualizado = estudianteRepository.save(estudiante);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return estudianteRepository.findById(id)
                .map(estudiante -> {
                    estudianteRepository.delete(estudiante);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
