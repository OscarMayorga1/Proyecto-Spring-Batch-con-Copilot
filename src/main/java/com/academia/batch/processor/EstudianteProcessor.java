package com.academia.batch.processor;

//Processor de Spring Batch que implementa ItemProcessor<Estudiante, Estudiante>, En el metodo process: calcula el promedio como (nota1 + nota2 + nota3) / 3, asigna el promedio al estudiante con setPromedio, registra un log SLF4J "Step 1 - Procesando: {estudiante}" y devuelve el estudiante

public class EstudianteProcessor implements org.springframework.batch.item.ItemProcessor<com.academia.batch.model.Estudiante, com.academia.batch.model.Estudiante> {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EstudianteProcessor.class);

    @Override
    public com.academia.batch.model.Estudiante process(com.academia.batch.model.Estudiante estudiante) throws Exception {

        //Excepcion para manejar el caso de que estudiante sea null
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser null");
        }

        double promedio = (estudiante.getNota1() + estudiante.getNota2() + estudiante.getNota3()) / 3;
        estudiante.setPromedio(promedio);
        log.info("Step 1 - Procesando: {}", estudiante);
        return estudiante;
    }
}

