package com.academia.batch.processor;

import com.academia.batch.model.EstudianteReporte;
import com.academia.batch.repository.EstudianteEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

class ReporteEstudianteProcessorTest {

    private final ReporteEstudianteProcessor processor = new ReporteEstudianteProcessor();

    @Test
    void process_debeMarcarAprobadoCuandoPromedioEs70() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setId(1L);
        estudiante.setNombre("Ana");
        estudiante.setGrupo("A");
        estudiante.setPromedio(70.0);

        EstudianteReporte reporte = Objects.requireNonNull(processor.process(estudiante));

        assertEquals(70.0, reporte.getPromedio(), 0.0001);
        assertEquals("APROBADO", reporte.getEstado());
    }

    @Test
    void process_debeMarcarReprobadoCuandoPromedioEs69_9() {
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setId(2L);
        estudiante.setNombre("Luis");
        estudiante.setGrupo("B");
        estudiante.setPromedio(69.9);

        EstudianteReporte reporte = Objects.requireNonNull(processor.process(estudiante));

        assertEquals(69.9, reporte.getPromedio(), 0.0001);
        assertEquals("REPROBADO", reporte.getEstado());
    }
}
