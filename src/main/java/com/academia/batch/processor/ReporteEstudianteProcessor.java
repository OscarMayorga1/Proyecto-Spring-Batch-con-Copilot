package com.academia.batch.processor;

// Processor que implementa ItemProcessor<Estudiante, EstudianteReporte>. Convierte un Estudiante en un EstudianteReporte copiando nombre, grupo y promedio,y asigna estado "APROBADO" si el promedio es >= 70, o "REPROBADO" si es menor.Loguea "Step 2 - Reporte: {reporte}" y devuelve el reporte.

import com.academia.batch.model.Estudiante;
import com.academia.batch.model.EstudianteReporte;
import com.academia.batch.repository.EstudianteEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class ReporteEstudianteProcessor implements ItemProcessor<EstudianteEntity, EstudianteReporte> {

    private static final Logger log = LoggerFactory.getLogger(ReporteEstudianteProcessor.class);

    @Override
    public EstudianteReporte process(EstudianteEntity estudiante) {
        EstudianteReporte reporte = new EstudianteReporte();
        reporte.setId(String.valueOf(estudiante.getId()));
        reporte.setNombre(estudiante.getNombre());
        reporte.setGrupo(estudiante.getGrupo());
        reporte.setPromedio(estudiante.getPromedio());
        reporte.setEstado(estudiante.getPromedio() >= 70 ? "APROBADO" : "REPROBADO");

        log.info("Step 2 - Reporte: {}", reporte);
        return reporte;
    }


}
