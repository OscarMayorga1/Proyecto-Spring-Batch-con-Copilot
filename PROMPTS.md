# PROMPTS.md

## Proyecto

**Nombre:** Spring Batch + IA Generativa

### Modalidades de generación de código utilizadas

Durante el desarrollo de este proyecto se utilizarán las siguientes modalidades de GitHub Copilot Pro:

| Modalidad | Descripción |
|-----------|-------------|
| 💬 Chat | Consultas realizadas mediante el chat de Copilot para generar código, explicar conceptos o proponer soluciones. |
| ✨ Autocompletado | Sugerencias automáticas generadas mientras se escribe código. |
| ⚡ Comandos | Generación de código mediante comandos rápidos o acciones disponibles en el editor (por ejemplo, `/fix`, `/tests`, `/explain`, etc.). |

---

# Registro de Prompts

## Prompt 001 : Generar el pom.xml con Copilot Chat

**Modalidad:** 💬 Chat

### Objetivo

Generar el archivo `pom.xml` para un proyecto Spring Boot 3.2.2 con Java 17 y las dependencias especificadas.
### Prompt

> Genera un pom.xml para un proyecto Spring Boot 3.2.2 con Java 17 y estas dependencias:
spring-boot-starter-batch, mysql-connector-j (scope runtime), spring-boot-starter-data-mongodb, springboot-starter-web, spring-boot-starter-data-jpa y spring-boot-starter-test (scope test). groupId
com.academia, artifactId spring-batch-final-calificaciones, versión 1.0.0. Incluye el spring-boot-mavenplugin.


### Resultado

✅ Aceptado.

### Cambios realizados

-  El prompt generó correctamente el archivo `pom.xml` con las dependencias y configuraciones solicitadas.
- Sin embargo se agrego la dependencia H2 para pruebas unitarias y de integración, ya que es una buena práctica incluir una base de datos en memoria para pruebas, evitando la necesidad de depender de una base de datos externa durante el desarrollo y las pruebas.
---
## Prompt 002: Generar el modelo Estudiante (POJO)

**Modalidad:** ✨ Autocompletado

### Objetivo

Crear el modelo de Estudiante a partir de campos y metodos descritos en las instrucciones, ademas de sobreescribir el metodo toString() para mostrar los datos del estudiante de manera legible.
### Prompt

>  En package com.academia.batch.model;
>
>   // Clase modelo Estudiante con los campos: nombre (String), grupo (String),
>   // nota1, nota2, nota3 y promedio (todos double).
> 
>   // Incluye constructor vacio, getters y setters de todos los campos,
>   // y un toString que muestre nombre, grupo y promedio.

### Resultado

⚠️ Modificado.

### Cambios realizados

- Se agrego el Id tipo long con sus getter y setters correspondientes, esto ya que sin este campo Mongo no tenia modo de identificar un documento unico.
---

## Prompt 003:  Generar EstudianteProcessor (lógica del Step 1)

**Modalidad:** ✨ Autocompletado

### Objetivo

Crea el procesador EstudianteProcessor que implemente la interfaz ItemProcessor de Spring Batch. Este procesador debe recibir un objeto Estudiante, calcular su promedio a partir de las notas (nota1, nota2 y nota3) y establecer el valor del promedio en el objeto Estudiante antes de devolverlo.
### Prompt

>  En package com.academia.batch.processor;
>
>   // Processor de Spring Batch que implementa ItemProcessor<Estudiante, Estudiante>.
>   // En el metodo process: calcula el promedio como (nota1 + nota2 + nota3) / 3,
>   // asigna el promedio al estudiante con setPromedio, registra un log con SLF4J
>   // "Step 1 - Procesando: {estudiante}" y devuelve el estudiante.


### Resultado

⚠️ Modificado.

### Cambios realizados

- No se realizaron cambios, el prompt generó correctamente la clase estudiante con los campos, métodos y el método `toString()` solicitado.
- Sin embargo se agrego un prompt para generar una excepcion para manejar los casos nulos con el siguiente PROMPT:
> //Excepcion para manejar el caso de que estudiante sea null
---

## Prompt 004: Generar EstudianteReporte (documento MongoDB)

**Modalidad:** ✨ Autocompletado

### Objetivo

Crear una entidad EstudianteReporte que represente un documento en MongoDB. Esta entidad debe contener los campos: nombre (String), grupo (String), promedio (double) y estado (String). Además, debe incluir un constructor vacío, getters y setters para todos los campos, y un método `toString()` que muestre el nombre, grupo y promedio y el estado del estudiante.
### Prompt

>  En package com.academia.batch.model;
>
>   // Clase modelo Estudiante con los campos: nombre (String), grupo (String),
>   // nota1, nota2, nota3 y promedio (todos double).
>
>   // Incluye constructor vacio, getters y setters de todos los campos,
>   // y un toString que muestre nombre, grupo y promedio.

### Resultado

✅ Aceptado.

### Cambios realizados

- No se realizaron cambios, el prompt generó correctamente la clase EstudianteReporte con los campos, métodos y el método `toString()` solicitado. Ademas de que agrego las librerias correspondientes para la anotacion @Document de MongoDB.
---

## Prompt 005: Generar ReporteEstudianteProcessor (lógica del Step 2)

**Modalidad:** ✨ Autocompletado

### Objetivo
Crea el procesador ReporteEstudianteProcessor que implemente la interfaz ItemProcessor de Spring Batch. Este procesador que convierte un Estudiante en un EstudianteReporte, determina su estado académico y registra la operación

### Prompt

>  En package com.academia.batch.processor;
> 
>  // Processor que implementa ItemProcessor<Estudiante, EstudianteReporte>.
>  // Convierte un Estudiante en un EstudianteReporte copiando nombre, grupo y promedio,
>  // y asigna estado "APROBADO" si el promedio es >= 70, o "REPROBADO" si es menor.
>  // Loguea "Step 2 - Reporte: {reporte}" y devuelve el reporte.


### Resultado

✅ Aceptado.

### Cambios realizados

- No se realizaron cambios, Copilot manejo perfectamente el umbral con la lógica solicitada.
---
## Prompt 006 :  Generar BatchConfig (Steps y Job)  con Copilot Chat

**Modalidad:** 💬 Chat

### Objetivo

Genera la clase de configuración BatchConfig que defina los Steps y el Job de Spring Batch, utilizando los procesadores EstudianteProcessor y ReporteEstudianteProcessor, así como los lectores y escritores correspondientes para manejar archivos CSV y documentos MongoDB.
### Prompt

> Genera una clase @Configuration de Spring Batch (Spring Boot 3.2) llamada BatchConfig con:
Step 1 "paso1": FlatFileItemReader que lee "estudiantes.csv" del classpath (delimitado, columnas
nombre,grupo,nota1,nota2,nota3, salta 1 linea, targetType Estudiante), procesa con EstudianteProcessor y escribe
en MySQL con JdbcBatchItemWriter (INSERT en estudiantes_procesados con
nombre,grupo,nota1,nota2,nota3,promedio, beanMapped).
Step 2 "paso2": JdbcCursorItemReader que hace SELECT nombre,grupo,promedio de estudiantes_procesados,
procesa con ReporteEstudianteProcessor y escribe en MongoDB con MongoItemWriter en la coleccion
"reportes_estudiantes".
Ambos steps con chunk de 3.
Un Job "procesarCalificacionesJob" con RunIdIncrementer que ejecuta paso1 y luego paso2. Usa la API de Spring
Batch 5 (JobBuilder y StepBuilder con JobRepository).

### Resultado

✅ Aceptado.

### Cambios realizados

- No se realizaron cambios, el prompt generó correctamente la clase BatchConfig con los Steps y Job solicitados, utilizando los procesadores, lectores y escritores correspondientes para manejar archivos CSV y documentos MongoDB.
---
## Prompt 007: Clase de arranque SpringBatchApplication

**Modalidad:** ✨ Autocompletado

### Objetivo

Crea  una clase principal de arranque para la aplicación Spring Boot, que contenga el método `main` y la anotación `@SpringBootApplication`, permitiendo iniciar la aplicación y ejecutar el Job de Spring Batch.
### Prompt

>  En package com.academia.batch;
>
>Clase principal de Spring Boot con @SpringBootApplication y el metodo main que arranca la aplicacion con SpringApplication.run.


### Resultado

✅ Aceptado.

### Cambios realizados

- No se realizaron cambios, el prompt generó correctamente la clase principal de arranque SpringBatchApplication con la anotación `@SpringBootApplication` y el método `main` que inicia la aplicación.
---

## Prompt 008:  Generar  Recursos: application.properties , tabla MySQL y CSV

**Modalidad:** 💬 Chat

### Objetivo

Se genera n el archivo `application.properties` la configuración necesaria para la conexión a MySQL y MongoDB, así como la configuración de Spring Batch. Además, se crea un script SQL para crear la tabla `estudiantes_procesados` en MySQL y un archivo CSV de ejemplo con datos de estudiantes.
### Prompt

>   Genera un application.properties para Spring Boot que se conecte a MySQL en
jdbc:mysql://localhost:3306/academia (usuario alumno, password alumno123), inicialice el esquema de Spring
Batch siempre, ejecute el Job al arrancar, y se conecte a MongoDB en
mongodb://root:root123@localhost:27018/academia?authSource=admin.

### Resultado

⚠️ Modificado.

### Cambios realizados

- Se realizaron cambios en el prompt original para utilizar H2 en lugar de MySQL para pruebas unitarias y de integración, ya que es una buena práctica incluir una base de datos en memoria para pruebas, evitando la necesidad de depender de una base de datos externa durante el desarrollo y las pruebas. Se agregaron las siguientes configuraciones al archivo `application.properties`:
#### Prompt

>Convierte esta configuración de application.properties para que el proyecto utilice una base de datos H2 en lugar de MySQL. Mantén la configuración de Spring Batch, habilita la consola web de H2, configura una base de datos persistente (archivo local) y conserva la configuración de MongoDB sin cambios. Explica brevemente cada propiedad agregada o modificada.

---
## Prompt 009: Entidad JPA

**Modalidad:** ✨ Autocompletado

### Objetivo

Crea la entidad JPA EstudianteProcesado que represente la tabla `estudiantes_procesados` en MySQL. Esta entidad debe contener los campos: id (Long, autogenerado), nombre (String), grupo (String), nota1 (double), nota2 (double), nota3 (double) y promedio (double). Además, debe incluir un constructor vacío, getters y setters para todos los campos, y un método `toString()` que muestre el nombre, grupo y promedio del estudiante.
### Prompt

>  package com.academia.batch.repository;
>
>   Entidad JPA (@Entity, @Table name="estudiantes_procesados") que mapea la tabla existente.
>   id Long con @Id y @GeneratedValue(IDENTITY); campos nombre, grupo, nota1, nota2, nota3,
>   promedio; getters y setters.



### Resultado

✅ Aceptado.

### Cambios realizados

- No se realizaron cambios, la entidad JPA EstudianteProcesado fue generada correctamente con los campos, anotaciones y métodos solicitados.
---
## Prompt 010: Repositorio EstudianteRepository

**Modalidad:** ✨ Autocompletado

### Objetivo

Crea el repositorio EstudianteRepository que extienda JpaRepository para la entidad EstudianteProcesado, permitiendo realizar operaciones CRUD en la tabla `estudiantes_procesados` de MySQL.
### Prompt

>  En package com.academia.batch.repository;
>
>   Interfaz EstudianteRepository que extiende JpaRepository<EstudianteEntity, Long>
>   con un metodo findByGrupo(String grupo) que devuelve List<EstudianteEntity>.


### Resultado

✅ Aceptado.

### Cambios realizados

- No se realizaron cambios, el prompt generó correctamente la interfaz EstudianteRepository que extiende JpaRepository y contiene el método findByGrupo(String grupo) que devuelve una lista de EstudianteEntity.
---
## Prompt 011: Repositorio ReporteRepository

**Modalidad:** ✨ Autocompletado

### Objetivo

Crea el repositorio ReporteRepository que extienda MongoRepository para la entidad EstudianteReporte, permitiendo realizar operaciones CRUD en la colección `reportes_estudiantes` de MongoDB.
### Prompt

>  En package com.academia.batch.repository;
>
>   Interfaz ReporteRepository que extiende MongoRepository<EstudianteReporte, String>
>   con un metodo findByEstado(String estado) que devuelve List<EstudianteReporte>.


### Resultado

⚠️ Modificado.

### Cambios realizados

- Se realizo un cambio ya que aunque el prompt genero correctamente la interfaz hizo una escritura "fully Qualified Class Name" uso la ruta completa del paquete de Mongodb directamente en la extensión de la interfaz, lo cual no es necesario si ya se tiene importada la clase EstudianteReporte. Se corrigió para que extienda MongoRepository<EstudianteReporte, String> sin necesidad de usar la ruta completa del paquete.
---
## Prompt 012: Servicio EstudianteService

**Modalidad:** ✨ Autocompletado

### Objetivo

Crea el servicio EstudianteService que utilice el repositorio EstudianteRepository para contar el número de estudiantes que tienen promedio aprobado (>= 70) y reprobado (< 70), y devuelva un objeto con estos conteos.
### Prompt

>  package com.academia.batch.service.EstudianteService;
>
>   // @Service con inyeccion por constructor de EstudianteRepository.
>   // Metodo contarAprobados() que devuelve cuantos estudiantes tienen promedio >= 70,
>   // usando findAll() y un stream con filter y count.



### Resultado

✅ Aceptado.

### Cambios realizados

- No se realizaron cambios, el prompt generó correctamente la clase EstudianteService con la inyección por constructor de EstudianteRepository y el método contarAprobados() que utiliza findAll() y un stream con filter y count para contar los estudiantes aprobados.
---
## Prompt 013: Controlador EstudianteController

**Modalidad:** 💬 Chat

### Objetivo

Genera un controlador REST EstudianteController que exponga endpoints para obtener el número de estudiantes aprobados, utilizando el servicio EstudianteService. El controlador debe manejar las solicitudes HTTP GET y devolver los resultados en formato JSON. Ademas de manejar otros metodos HTTP como POST, PUT y DELETE para la entidad EstudianteProcesado, utilizando el repositorio EstudianteRepository.
### Prompt

>  En package com.academia.batch.repository;
>
>   Genera un @RestController en /api/estudiantes que use
EstudianteRepository y EstudianteService (inyectados por constructor) con: GET / (listar todos), GET /{id}
(200 o 404), GET /aprobados/total (devuelve un Map con el conteo del servicio), POST / (crea, 201 Created),
PUT /{id} (reemplaza, 200 o 404), PATCH /{id} (cambia solo el grupo desde un Map, 200 o 404), DELETE /{id}
(204 o 404). Usa ResponseEntity para los codigos de respuesta.


### Resultado

✅ Aceptado.

### Cambios realizados

- No se realizaron cambios, el prompt generó correctamente la clase EstudianteController con los endpoints solicitados, utilizando el servicio EstudianteService y el repositorio EstudianteRepository para manejar las operaciones CRUD y el conteo de estudiantes aprobados. Ademas de manejar los códigos de respuesta HTTP utilizando ResponseEntity.
---
## Prompt 014: Controlador ReporteController

**Modalidad:** ✨ Autocompletado

### Objetivo

Crea un controlador REST ReporteController que exponga endpoints para obtener reportes de estudiantes desde MongoDB, utilizando el repositorio ReporteRepository. El controlador debe manejar las solicitudes HTTP GET y devolver los resultados en formato JSON, permitiendo filtrar por estado académico.
### Prompt

>  En package com.academia.batch.repository;
>
>   // @RestController en /api/reportes que usa ReporteRepository:
>    GET / lista todos los reportes; GET /estado/{estado} devuelve los que tengan ese estado
>    (convertido a mayusculas) usando findByEstado.


### Resultado

✅ Aceptado.

### Cambios realizados

- No se realizaron cambios, 
- el prompt generó correctamente la clase ReporteController con los endpoints solicitados, utilizando el repositorio ReporteRepository para manejar las operaciones de obtención de reportes y filtrado por estado académico.