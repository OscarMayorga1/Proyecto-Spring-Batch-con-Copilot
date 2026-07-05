package com.academia.batch.config;

import com.academia.batch.model.Estudiante;
import com.academia.batch.model.EstudianteReporte;
import com.academia.batch.processor.EstudianteProcessor;
import com.academia.batch.processor.ReporteEstudianteProcessor;
import javax.sql.DataSource;

import com.academia.batch.repository.EstudianteEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    public EstudianteProcessor estudianteProcessor() {
        return new EstudianteProcessor();
    }

    @Bean
    public ReporteEstudianteProcessor reporteEstudianteProcessor() {
        return new ReporteEstudianteProcessor();
    }

    @Bean
    public FlatFileItemReader<Estudiante> estudianteReader() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("nombre", "grupo", "nota1", "nota2", "nota3");

        BeanWrapperFieldSetMapper<Estudiante> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Estudiante.class);

        DefaultLineMapper<Estudiante> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return new FlatFileItemReaderBuilder<Estudiante>()
                .name("estudianteReader")
                .resource(new ClassPathResource("estudiantes.csv"))
                .linesToSkip(1)
                .lineMapper(lineMapper)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Estudiante> estudianteWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Estudiante>()
                .dataSource(dataSource)
                .sql("INSERT INTO estudiantes_procesados (nombre, grupo, nota1, nota2, nota3, promedio) VALUES (:nombre, :grupo, :nota1, :nota2, :nota3, :promedio)")
                .beanMapped()
                .build();
    }

    // CORRECCIÓN 1: Cambiado a EstudianteEntity y se agregó el 'id' en el SELECT SQL
    @Bean
    public JdbcCursorItemReader<EstudianteEntity> reporteReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<EstudianteEntity>()
                .name("reporteReader")
                .dataSource(dataSource)
                .sql("SELECT id, nombre, grupo, promedio FROM estudiantes_procesados") // ◄ Agregado el ID aquí
                .rowMapper(new BeanPropertyRowMapper<>(EstudianteEntity.class)) // ◄ Mapeador a Entity
                .build();
    }

    @Bean
    public MongoItemWriter<EstudianteReporte> reporteWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<EstudianteReporte>()
                .template(mongoTemplate)
                .collection("reportes_estudiantes")
                .build();
    }

    @Bean
    public Step paso1(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<Estudiante> estudianteReader,
            EstudianteProcessor estudianteProcessor,
            JdbcBatchItemWriter<Estudiante> estudianteWriter
    ) {
        return new StepBuilder("paso1", jobRepository)
                .<Estudiante, Estudiante>chunk(3, transactionManager)
                .reader(estudianteReader)
                .processor(estudianteProcessor)
                .writer(estudianteWriter)
                .build();
    }

    // CORRECCIÓN 2: Tipos alineados en los parámetros de entrada del método
    @Bean
    public Step paso2(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JdbcCursorItemReader<EstudianteEntity> reporteReader, // ◄ Sincronizado
            ReporteEstudianteProcessor reporteEstudianteProcessor,
            MongoItemWriter<EstudianteReporte> reporteWriter
    ) {
        return new StepBuilder("paso2", jobRepository)
                .<EstudianteEntity, EstudianteReporte>chunk(3, transactionManager) // ◄ Sincronizado
                .reader(reporteReader)
                .processor(reporteEstudianteProcessor)
                .writer(reporteWriter)
                .build();
    }

    @Bean
    public Job procesarCalificacionesJob(JobRepository jobRepository, Step paso1, Step paso2) {
        return new JobBuilder("procesarCalificacionesJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(paso1)
                .next(paso2)
                .build();
    }
}