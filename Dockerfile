# Dockerfile para Payments API
# Imagen base con Java 17
FROM eclipse-temurin:17-jre-alpine

# Metadata
LABEL maintainer="testkoa"
LABEL description="API de Gestión de Pagos"

# Directorio de trabajo
WORKDIR /app

# Copiar JAR de la aplicación
COPY target/payments-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto de la aplicación
EXPOSE 8080

# Variables de entorno por defecto (se pueden sobrescribir)
ENV JAVA_OPTS=""

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
