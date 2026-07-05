package com.academia;

import com.academia.batch.model.Estudiante;
import com.academia.batch.processor.EstudianteProcessor;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        //Generar un codigo para probar mi modelo y processor
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre("Juan");
        estudiante.setGrupo("A");
        estudiante.setNota1(8.0);
        estudiante.setNota2(9.0);
        estudiante.setNota3(10.0);

        EstudianteProcessor processor = new EstudianteProcessor();

        Estudiante resultado = processor.process(estudiante);
        System.out.println("Resultado: " + resultado);
    }
}