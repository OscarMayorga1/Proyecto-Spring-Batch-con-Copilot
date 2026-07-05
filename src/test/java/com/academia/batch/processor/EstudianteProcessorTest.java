package com.academia.batch.processor;

import com.academia.batch.model.Estudiante;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstudianteProcessorTest {

    private final EstudianteProcessor processor = new EstudianteProcessor();

    @Test
    void process_debeCalcularPromedioCorrectamente() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setNota1(80.0);
        estudiante.setNota2(70.0);
        estudiante.setNota3(60.0);

        Estudiante resultado = Objects.requireNonNull(processor.process(estudiante));

        assertEquals(70.0, resultado.getPromedio(), 0.0001);
    }
}
