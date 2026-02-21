Desarrollo API – Gestión de Pagos
Construir un microservicio con los siguientes endpoints:
1.	POST /payments
Recibe el siguiente JSON:
{
  "numeroCredito": "12345",
  "valor": 250000,
  "fecha": "2026-02-18"
}
Reglas obligatorias:
•	valor debe ser mayor a 0
•	numeroCredito es obligatorio
•	No permitir pagos duplicados (idempotencia por numeroCredito + fecha + valor)
•	Manejar errores con códigos HTTP adecuados (400, 409, 500)

2.	GET /payments/{numeroCredito}
Retorna la lista de pagos del crédito indicado, ordenados por fecha.
Si el crédito no existe → responder 404.
3.	GET /health
Debe responder simplemente: OK.
Requisitos técnicos mínimos:
•	Java 17+ y Spring Boot
•	 Arquitectura en capas (controller, service, repository)
•	 Manejo global de excepciones
•	 Logs controlados (sin exponer stacktrace innecesario)
3. Base de Datos y SQL
Utilizar PostgreSQL. Entregar archivo schema.sql que incluya:
•	Tabla payments 
•	Restricción o mecanismo para evitar duplicados
Entregar, además:
•	Consulta: Total pagado por numero_credito
•	 Consulta: Top 5 créditos con mayor pago en un rango de fechas
•	 Justificación breve de los índices creados
4. Seguridad Básica
Proteger todos los endpoints (excepto /health) mediante API Key.
•	Header obligatorio: X-API-KEY
•	El valor debe configurarse por variable de entorno.
•	Si es inválido o no se envía → responder 401.
•	No se permite hardcodear secretos.
5. Dockerización
Entregar:
•	Dockerfile
•	docker-compose.yml que levante aplicación y PostgreSQL
Debe ejecutarse correctamente con: docker compose up
