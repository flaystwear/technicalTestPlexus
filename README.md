# technicalTestPlexus
Prueba técnica Plexus

## Proyecto Maven (Java 21)

Este repositorio incluye una aplicación Spring Boot (Java 21).

### Requisitos
- Java 21 (JDK 21)
- Apache Maven 3.9+

### Construir
```bash
mvn -v
mvn clean verify
```

### Ejecutar (modo desarrollo)
```bash
mvn spring-boot:run
```

### Empaquetar y ejecutar el JAR
```bash
mvn -q -DskipTests package
java -jar target/technicalTestPlexus-0.0.1-SNAPSHOT.jar
```

### Probar
```bash
mvn test
```

### Estructura
- `pom.xml`: configuración de Maven y Java 21
- `src/main/java/com/plexus/app/App.java`: punto de entrada (`@SpringBootApplication`)
- `src/main/java/com/plexus/app/web/HelloController.java`: controlador GET `/hello`
- `src/test/java`: pruebas

### Probar el endpoint
```bash
curl http://localhost:8080/hello
```