-- Schema para API de Gestión de Pagos
-- Tabla: payments

-- Eliminar tabla si existe (para desarrollo)
DROP TABLE IF EXISTS payments;

-- Crear tabla de pagos
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    numero_credito VARCHAR(50) NOT NULL,
    valor DECIMAL(15, 2) NOT NULL CHECK (valor > 0),
    fecha DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraint de idempotencia: previene pagos duplicados
    CONSTRAINT uk_idempotency UNIQUE (numero_credito, fecha, valor)
);

-- Índice para optimizar consulta GET /payments/{numeroCredito}
-- Justificación: Esta consulta filtra por numero_credito y ordena por fecha DESC.
-- El índice compuesto permite búsqueda eficiente y ordenamiento sin escaneo completo.
CREATE INDEX idx_numero_credito_fecha ON payments(numero_credito, fecha DESC);

-- Índice para optimizar consultas de rango de fechas y ordenamiento por valor
-- Justificación: Para la consulta de Top 5 créditos con mayor pago en un rango de fechas,
-- este índice permite filtrar eficientemente por rango de fechas y ordenar por valor.
CREATE INDEX idx_fecha_valor ON payments(fecha, valor DESC);

-- ============================================================================
-- CONSULTAS SQL DE REPORTE
-- ============================================================================

-- 1. Consulta: Total pagado por número de crédito
-- Descripción: Suma todos los pagos realizados para un crédito específico
-- Uso: SELECT numero_credito, SUM(valor) as total_pagado
--      FROM payments
--      WHERE numero_credito = '12345'
--      GROUP BY numero_credito;

-- 2. Consulta: Top 5 créditos con mayor pago en un rango de fechas
-- Descripción: Retorna los 5 créditos con el pago individual más alto en un período
-- Uso: SELECT numero_credito, MAX(valor) as mayor_pago, COUNT(*) as cantidad_pagos
--      FROM payments
--      WHERE fecha BETWEEN '2026-01-01' AND '2026-12-31'
--      GROUP BY numero_credito
--      ORDER BY mayor_pago DESC
--      LIMIT 5;

-- Variante alternativa - Total acumulado por crédito en rango de fechas:
-- Uso: SELECT numero_credito, SUM(valor) as total_pagado, COUNT(*) as cantidad_pagos
--      FROM payments
--      WHERE fecha BETWEEN '2026-01-01' AND '2026-12-31'
--      GROUP BY numero_credito
--      ORDER BY total_pagado DESC
--      LIMIT 5;

-- ============================================================================
-- JUSTIFICACIÓN DE ÍNDICES
-- ============================================================================

-- idx_numero_credito_fecha (numero_credito, fecha DESC):
-- - Propósito: Optimizar GET /payments/{numeroCredito}
-- - Beneficio: Permite búsqueda directa por numero_credito (primera columna del índice)
--   y ordenamiento eficiente por fecha descendente sin ORDER BY adicional
-- - Casos de uso: Consultas frecuentes para listar todos los pagos de un crédito
-- - Impacto: Reduce tiempo de consulta de O(n log n) a O(log n) para búsqueda + O(k) para ordenar

-- idx_fecha_valor (fecha, valor DESC):
-- - Propósito: Optimizar consultas de reporte por rangos de fechas
-- - Beneficio: Permite filtrado eficiente por rango de fechas (BETWEEN) y ordenamiento por valor
-- - Casos de uso: Reportes de top N créditos en períodos específicos
-- - Impacto: Evita escaneo completo de tabla para reportes analíticos

-- Nota: No se crea índice adicional solo en numero_credito porque el índice compuesto
-- idx_numero_credito_fecha ya cubre esas búsquedas (leftmost prefix rule en PostgreSQL)
