# API de Gestión de Pagos

Microservicio REST para gestión de pagos de créditos, desarrollado con Spring Boot 4.0.3 y Java 17.



# Solución punto 6.
**Caso de Producción**

**1. Si aparecen errores 500 intermitentes ¿qué se revisaría primero?**
    Identificaría de qué servicio provienen dicho errores, para ello revisaría los logs para tener un contexto más detallado. Luego, prosigo con los pasos posteriores para determinar qué está causando los errores. A partir de este análisis identificaría tiempos, ids, requests, adevertencias, response, frecuencia de los errores, datos. De acuerdo con lo anterior, con un análisis de 30 minutos llegaría a hacer suficiente para determinar la gravedad, el origen, posibles consecuencias de no ser solucionado.

**2. Si la base de datos responde lento. ¿ qué pasos técnicos tomaría?**
   Si la base de datos responde lento, posiblemente haya alguna consulta o tabla bloqueada, esto podría ser a causa de muchas peticiones, falta de espacio, límite de pool de conexiones, etc...
   Dependiente de lo que se encuentre, seguiría estos pasos:
   2.1. Revisar métricas del gestor de base de datos
   2.2 En caso de que sea una consulta el objetivo sería encontrar en dónde está esa consulta y qué usuario la está ejecutando
   2.3 Tomar tiempos, el usuario, el origen y posibles IDs
   2.4 En caso de que se identifique que es una consulta lenta y está tomando mucho tiempo en responder el paso técnico a tomar es una posible optimización o refactorización de dicha consulta, por ejemplo: 
   En PosgresSQL puedo usar *EXPLAIN ANALIZE* para obtener estadísticas, rutas, índices, bucles, que se encuentren haciendo la consulta y así más fácil obtener una posible refactorización.

**3. ¿Qué métricas básicas monitorearías en un sistema financiero?**
En un sistema financiero es importante garantizar la confiabilidad y operabilidad del sistema, por lo tanto, las métricas ayudarían a determinar tiempos, errores, advertencias por ejemplo de la respuesta de las funcionalidades, dentro de las métricas básicas monitorearía las siguientes:
    3.1 Funcionamiento correcto de los sistemas core del sistema financiero, por ejemplo: de cada API monitorear la cantidad de respuestas 4xx, 5xx y su respectiva trazabilidad
    3.2 Dependiendo de la lógica del negocio, identificar rango de valores dentro de los datos tipicos que estos no estén fuera de datos atípicos, desembolsos "irregulares", cuentas contables incorrectas, clientes y/o cuentas inactivos

# Solución punto 7
**Uso de herramientas de IA**
- Para el presente proyecto se utilizó herramientas de IA, como agentes y chat de Gemini.
- La IA dentro del proyecto se usó para realizar una adecuada planeación de arquitectura, entendimiento de los requerimientos y solución de errores con tecnologías utilizadas
- Para validar el código generado se usó un agente de IA dentro del editor de código Visual Studio Code en modo planeación inicialmente, esto con el objetivo de poder validar por parte mía el paso a paso que la herramienta IA iba a implementar, entonces en este punto se valida por ejemplo: versiones, métodos, arquitectura, scripts. Una vez finaliado lo anterior, cuando estuve de acuerdo con lo planteado por el agente, se procede a la implementación inicial, luego se procede a codificar y ejecutar el proyecto por parte mía; cuando se presentaron errores, se utilizaba el agente IA para identificar estos errores, entenderlos y lograr solucionarlos. 




##  Características

- ✅ API RESTful con endpoints para crear y consultar pagos
- ✅ Validación de datos de entrada
- ✅ Idempotencia para prevenir pagos duplicados
- ✅ Seguridad mediante API Key
- ✅ Manejo global de excepciones
- ✅ Base de datos PostgreSQL
- ✅ Dockerización completa
- ✅ Arquitectura en capas (Controller → Service → Repository)

##  Inicio Rápido

### Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- Docker y Docker Compose

### Construcción del Proyecto

```bash
# Compilar y empaquetar la aplicación
Visual estudio
./mvnw clean package -DskipTests
./mvnw clean package

mvn clean package -DskipTests
mvn clean package

```

### Ejecución con Docker

```bash
# Levantar todos los servicios (PostgreSQL + Aplicación)
docker compose up

# Para reconstruir las imágenes
docker compose up --build

# Para detener los servicios
docker compose down

# Para detener y eliminar volúmenes (limpieza completa)
docker compose down -v
```

La aplicación está disponible en: `http://localhost:8080`

## 📡 Endpoints

### Health Check

**GET** `/health`

Health check sin autenticación.

```bash
curl http://localhost:8080/health
```

**Respuesta:**
```
OK
```

---

### Crear Pago

**POST** `/payments`

Crea un nuevo pago. Requiere API Key.

**Headers:**
- `X-API-KEY`: API key de autenticación
- `Content-Type`: application/json

**Body:**
```json
{
  "numeroCredito": "12345",
  "valor": 250000,
  "fecha": "2026-02-18"
}
```

**Validaciones:**
- `numeroCredito`: obligatorio
- `valor`: obligatorio, debe ser mayor a 0
- `fecha`: obligatoria, formato yyyy-MM-dd

**Ejemplo:**
```bash
curl -X POST http://localhost:8080/payments \
  -H "X-API-KEY: my-secret-api-key" \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCredito": "12345",
    "valor": 250000,
    "fecha": "2026-02-18"
  }'
```

**Respuestas:**

✅ **201 Created** - Pago creado exitosamente
```json
{
  "id": 1,
  "numeroCredito": "12345",
  "valor": 250000,
  "fecha": "2026-02-18"
}
```

❌ **400 Bad Request** - Datos inválidos
```json
{
  "status": 400,
  "message": "valor: El valor debe ser mayor a 0",
  "timestamp": "2026-02-20T10:30:00"
}
```

❌ **401 Unauthorized** - API Key inválida o ausente
```json
{
  "status": 401,
  "message": "API Key es requerida. Incluya el header X-API-KEY"
}
```

❌ **409 Conflict** - Pago duplicado (idempotencia)
```json
{
  "status": 409,
  "message": "Ya existe un pago con los mismos datos (número de crédito, fecha y valor)",
  "timestamp": "2026-02-20T10:30:00"
}
```

---

### Consultar Pagos por Crédito

**GET** `/payments/{numeroCredito}`

Retorna todos los pagos de un crédito específico, ordenados por fecha ascendente.

**Headers:**
- `X-API-KEY`: API key de autenticación

**Ejemplo:**
```bash
curl -H "X-API-KEY: my-secret-api-key" \
  http://localhost:8080/payments/12345
```

**Respuestas:**

✅ **200 OK** - Pagos encontrados
```json
[
  {
    "id": 1,
    "numeroCredito": "12345",
    "valor": 250000,
    "fecha": "2026-02-18"
  },
  {
    "id": 2,
    "numeroCredito": "12345",
    "valor": 150000,
    "fecha": "2026-02-19"
  }
]
```

❌ **401 Unauthorized** - API Key inválida o ausente

❌ **404 Not Found** - No hay pagos para el crédito
```json
{
  "status": 404,
  "message": "No se encontraron pagos para el crédito: 99999",
  "timestamp": "2026-02-20T10:30:00"
}
```

##  Seguridad

Todos los endpoints excepto `/health` requieren autenticación mediante API Key.

**Header requerido:**
```
X-API-KEY: my-secret-api-key
```

La API Key se configura mediante variable de entorno `API_KEY`.

## ⚙️ Configuración

### Variables de Entorno

| Variable | Descripción | Default | Requerida |
|----------|-------------|---------|-----------|
| `API_KEY` | Clave de autenticación para endpoints | `default-dev-key` | SI |
| `SPRING_DATASOURCE_URL` | URL de conexión PostgreSQL | `jdbc:postgresql://localhost:5432/paymentsdb` | SI |
| `DB_USERNAME` | Usuario de base de datos | `postgres` | SI |
| `DB_PASSWORD` | Contraseña de base de datos | `postgres` | SI|
| `JAVA_OPTS` | Opciones JVM | - | NO |

### Modificar API Key

Editar en [docker-compose.yml](docker-compose.yml):
```yaml
services:
  app:
    environment:
      API_KEY: tu-nueva-api-key-secreta
```

O ejecutar con variable de entorno:
```bash
API_KEY=tu-api-key docker compose up
```

## 🗄️ Base de Datos

### Esquema

Ver archivo completo: [schema.sql](src/main/resources/schema.sql)

**Tabla `payments`:**
```sql
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    numero_credito VARCHAR(50) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL CHECK (valor > 0),
    fecha DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_idempotency UNIQUE (numero_credito, fecha, valor)
);
```

### Índices

**`idx_numero_credito_fecha`**: Optimiza consultas GET por número de crédito con ordenamiento por fecha.

**`idx_fecha_valor`**: Optimiza consultas de reporte por rango de fechas.

### Consultas SQL de Reporte

#### Total pagado por número de crédito

```sql
SELECT numero_credito, SUM(valor) as total_pagado
FROM payments
WHERE numero_credito = '12345'
GROUP BY numero_credito;
```

#### Top 5 créditos con mayor pago en rango de fechas

```sql
SELECT numero_credito, MAX(valor) as mayor_pago, COUNT(*) as cantidad_pagos
FROM payments
WHERE fecha BETWEEN '2026-01-01' AND '2026-12-31'
GROUP BY numero_credito
ORDER BY mayor_pago DESC
LIMIT 5;
```

#### Top 5 créditos con mayor total acumulado

```sql
SELECT numero_credito, SUM(valor) as total_pagado, COUNT(*) as cantidad_pagos
FROM payments
WHERE fecha BETWEEN '2026-01-01' AND '2026-12-31'
GROUP BY numero_credito
ORDER BY total_pagado DESC
LIMIT 5;
```

### Conexión Directa a PostgreSQL

```bash
# Conectar al contenedor
docker exec -it payments-postgres psql -U postgres -d paymentsdb

# Ver todas las tablas
\dt

# Ver estructura de tabla payments
\d payments

# Ejecutar consultas
SELECT * FROM payments;
```

## 🏗️ Arquitectura

### Estructura de Capas

```
Controller (REST API)
    ↓
Service (Lógica de Negocio)
    ↓
Repository (Acceso a Datos)
    ↓
Entity (Modelo de Datos)
```

### Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/testkoa/payments/
│   │   ├── PaymentsApplication.java       # Clase principal
│   │   ├── config/
│   │   │   └── SecurityConfig.java        # Configuración de seguridad
│   │   ├── controller/
│   │   │   ├── HealthController.java      # Health check endpoint
│   │   │   └── PaymentController.java     # Endpoints de pagos
│   │   ├── dto/
│   │   │   ├── ErrorResponseDTO.java      # DTO de respuesta de errores
│   │   │   ├── PaymentRequestDTO.java     # DTO de entrada
│   │   │   └── PaymentResponseDTO.java    # DTO de salida
│   │   ├── entity/
│   │   │   └── Payment.java               # Entidad JPA
│   │   ├── exception/
│   │   │   ├── DuplicatePaymentException.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── PaymentNotFoundException.java
│   │   ├── repository/
│   │   │   └── PaymentRepository.java     # Repositorio JPA
│   │   ├── security/
│   │   │   └── ApiKeyFilter.java          # Filtro de autenticación
│   │   └── service/
│   │       ├── PaymentService.java        # Interfaz de servicio
│   │       └── PaymentServiceImpl.java    # Implementación
│   └── resources/
│       ├── application.properties         # Configuración
│       └── schema.sql                     # Schema de base de datos
└── test/
    └── java/com/testkoa/payments/
        └── PaymentsApplicationTests.java
```

##  Pruebas

### Casos de Prueba

#### 1. Health Check
```bash
curl http://localhost:8080/health
# Esperado: "OK"
```

#### 2. POST sin API Key (debe fallar)
```bash
curl -X POST http://localhost:8080/payments \
  -H "Content-Type: application/json" \
  -d '{"numeroCredito":"12345","valor":250000,"fecha":"2026-02-18"}'
# Esperado: 401 Unauthorized
```

#### 3. Crear pago válido
```bash
curl -X POST http://localhost:8080/payments \
  -H "X-API-KEY: my-secret-api-key" \
  -H "Content-Type: application/json" \
  -d '{"numeroCredito":"12345","valor":250000,"fecha":"2026-02-18"}'
# Esperado: 201 Created
```

#### 4. Intentar duplicar pago (idempotencia)
```bash
# Repetir el comando anterior
# Esperado: 409 Conflict
```

#### 5. Crear pago con valor inválido
```bash
curl -X POST http://localhost:8080/payments \
  -H "X-API-KEY: my-secret-api-key" \
  -H "Content-Type: application/json" \
  -d '{"numeroCredito":"12345","valor":-100,"fecha":"2026-02-18"}'
# Esperado: 400 Bad Request
```

#### 6. Consultar pagos existentes
```bash
curl -H "X-API-KEY: my-secret-api-key" \
  http://localhost:8080/payments/12345
# Esperado: 200 OK con lista de pagos
```

#### 7. Consultar crédito sin pagos
```bash
curl -H "X-API-KEY: my-secret-api-key" \
  http://localhost:8080/payments/99999
# Esperado: 404 Not Found
```

## 🔧 Desarrollo Local

### Ejecución sin Docker

1. Instalar PostgreSQL localmente
2. Crear base de datos:
```sql
CREATE DATABASE paymentsdb;
```

3. Configurar variables de entorno:
```bash
export API_KEY=mi-api-key-local
export DB_USERNAME=postgres
export DB_PASSWORD=tu_password
```

4. Ejecutar aplicación:
```bash
mvn spring-boot:run
```

##  Decisiones Técnicas

### Lombok
Se utiliza Lombok para reducir boilerplate code (`@Data`, `@Builder`, `@Slf4j`).

### Idempotencia
Implementada mediante constraint única en base de datos `(numero_credito, fecha, valor)`. Garantiza prevención de duplicados incluso con requests concurrentes.

### Manejo de Errores
- `@ControllerAdvice` para manejo global
- `server.error.include-stacktrace=never` para no exponer detalles internos
- Logs completos en servidor, mensajes genéricos al cliente

### API Key Simple
Implementada con filtro de Servlet sin Spring Security completo, suficiente para el nivel de seguridad requerido.

### BigDecimal
Usado para valores monetarios para evitar problemas de precisión con floating-point.

### LocalDate
Sin componente de tiempo (timezone-agnostic) para simplificar lógica de idempotencia.

##  Tecnologías

- **Java 17**
- **Spring Boot 4.0.3**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
- **PostgreSQL 16**
- **Lombok**
- **Maven**
- **Docker & Docker Compose**

