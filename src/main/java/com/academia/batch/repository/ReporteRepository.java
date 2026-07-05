package com.academia.batch.repository;
// Interfaz ReporteRepository que extiende MongoRepository<EstudianteReporte, String> con un metodo findByEstado(String estado) que devuelve List<EstudianteReporte>.

import com.academia.batch.model.EstudianteReporte;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReporteRepository extends MongoRepository<EstudianteReporte, String> {
    // Spring Data crea la consulta automaticamente a partir del nombre del metodo
    List<EstudianteReporte> findByEstado(String estado);
}
