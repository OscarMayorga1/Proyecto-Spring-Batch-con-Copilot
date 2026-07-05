package com.academia.batch.service;

// ◄ IMPORTAMOS LA ENTIDAD CORRECTA
import com.academia.batch.repository.EstudianteEntity;
import com.academia.batch.repository.EstudianteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @InjectMocks
    private EstudianteService estudianteService;

    // ◄ Cambiado a EstudianteEntity
    private List<EstudianteEntity> estudiantesMock;

    @BeforeEach
    public void setUp() {
        // Crear 3 estudiantes usando la entidad correcta de JPA
        EstudianteEntity estudiante1 = new EstudianteEntity();
        estudiante1.setId(1L);
        estudiante1.setNombre("Juan Pérez");
        estudiante1.setGrupo("A");
        estudiante1.setPromedio(85.0); // Aprobado

        EstudianteEntity estudiante2 = new EstudianteEntity();
        estudiante2.setId(2L);
        estudiante2.setNombre("María García");
        estudiante2.setGrupo("A");
        estudiante2.setPromedio(72.5); // Aprobado

        EstudianteEntity estudiante3 = new EstudianteEntity();
        estudiante3.setId(3L);
        estudiante3.setNombre("Carlos López");
        estudiante3.setGrupo("B");
        estudiante3.setPromedio(65.0); // Reprobado

        estudiantesMock = Arrays.asList(estudiante1, estudiante2, estudiante3);
    }

    @Test
    public void testContarAprobados() {
        // Arrange: Ahora coincide perfectamente con lo que espera el repositorio real
        when(estudianteRepository.findAll()).thenReturn(estudiantesMock);

        // Act
        long resultado = estudianteService.contarAprobados();

        // Assert
        assertEquals(2L, resultado, "Debe haber exactamente 2 estudiantes aprobados");

        // Verify
        verify(estudianteRepository, times(1)).findAll();
    }

    @Test
    public void testContarAprobadosConListaVacia() {
        // Arrange
        when(estudianteRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        long resultado = estudianteService.contarAprobados();

        // Assert
        assertEquals(0L, resultado, "Debe devolver 0 cuando no hay estudiantes");
    }

    @Test
    public void testContarAprobadosTodosAprobados() {
        // Arrange: Cambiado a EstudianteEntity
        EstudianteEntity est1 = new EstudianteEntity();
        est1.setPromedio(90.0);
        EstudianteEntity est2 = new EstudianteEntity();
        est2.setPromedio(75.0);

        when(estudianteRepository.findAll()).thenReturn(Arrays.asList(est1, est2));

        // Act
        long resultado = estudianteService.contarAprobados();

        // Assert
        assertEquals(2L, resultado, "Debe devolver 2 cuando todos están aprobados");
    }
}
