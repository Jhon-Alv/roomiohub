-- ==========================================
-- 1. DEFINICIÓN DE TIPOS (ENUMS)
-- ==========================================
CREATE TYPE ROL_USUARIO AS ENUM ('ADMIN', 'RECEPCION', 'LIMPIEZA');
CREATE TYPE ESTADO_HABITACION AS ENUM ('DISPONIBLE', 'OCUPADO', 'SUCIO', 'EN_LIMPIEZA', 'MANTENIMIENTO');
CREATE TYPE METODO_PAGO AS ENUM ('EFECTIVO', 'TARJETA', 'BILLETERA_DIGITAL');

-- ==========================================
-- 2. MÓDULO DE SEGURIDAD Y MULTI-TENANT
-- ==========================================

CREATE TABLE PLANES
(
    ID                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    NOMBRE                  VARCHAR(50)    NOT NULL UNIQUE,
    MAX_HABITACIONES        INTEGER        NOT NULL,
    MAX_USUARIOS            INTEGER        NOT NULL,
    MAX_SEDES               INTEGER        NOT NULL,
    FLAG_MODULO_PROMOCIONES BOOLEAN          DEFAULT FALSE,
    PRECIO_MENSUAL          DECIMAL(10, 2) NOT NULL,
    ACTIVO                  BOOLEAN          DEFAULT TRUE
);

INSERT INTO PLANES (NOMBRE, MAX_HABITACIONES, MAX_USUARIOS, MAX_SEDES, FLAG_MODULO_PROMOCIONES, PRECIO_MENSUAL)
VALUES ('BASICO',        20,  6,  1,  FALSE, 89.00),
       ('PROFESIONAL',   50,  15, 3,  TRUE,  169.00),
       ('ILIMITADO',     -1,  -1, -1, TRUE,  289.00);

CREATE TABLE CUENTAS
(
    ID                       UUID PRIMARY KEY            DEFAULT gen_random_uuid(),
    NOMBRE_COMERCIAL         VARCHAR(255)       NOT NULL,
    RUC                      VARCHAR(20) UNIQUE NOT NULL,
    PLAN_ID                  UUID               NOT NULL REFERENCES PLANES (ID),
    FECHA_INICIO_SUSCRIPCION TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FECHA_FIN_SUSCRIPCION    TIMESTAMP          NOT NULL,
    ESTADO_SUSCRIPCION       VARCHAR(50)                 DEFAULT 'ACTIVO'
);

-- ==========================================
-- 3. MÓDULO DE SEDES (MULTI-SEDE)
-- ==========================================

CREATE TABLE SEDES
(
    ID         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    TENANT_ID  UUID         NOT NULL REFERENCES CUENTAS (ID),
    NOMBRE     VARCHAR(255) NOT NULL,
    DIRECCION  VARCHAR(500),
    ACTIVO     BOOLEAN          DEFAULT TRUE,
    UNIQUE (TENANT_ID, NOMBRE)
);

-- ==========================================
-- 4. MÓDULO DE PERSONAL
-- ==========================================

CREATE TABLE USUARIOS
(
    ID                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    TENANT_ID             UUID         NOT NULL REFERENCES CUENTAS (ID),
    SEDE_ID               UUID                  REFERENCES SEDES (ID),
    USERNAME              VARCHAR(100) NOT NULL,
    PASSWORD              VARCHAR(255) NOT NULL,
    ROL                   ROL_USUARIO  NOT NULL,
    FLAG_CAMBIAR_PASSWORD BOOLEAN          DEFAULT TRUE,
    ACTIVO                BOOLEAN          DEFAULT TRUE,
    UNIQUE (TENANT_ID, USERNAME)
);

-- ==========================================
-- 5. MÓDULO DE CATÁLOGO (DATOS MAESTROS)
-- ==========================================
-- Catálogos globales: apuntan al TENANT_ID (heredados por todas las sedes)

CREATE TABLE CATEGORIAS
(
    ID                UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    TENANT_ID         UUID           NOT NULL REFERENCES CUENTAS (ID),
    NOMBRE            VARCHAR(100)   NOT NULL,
    PRECIO_HORA_EXTRA DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    ACTIVO            BOOLEAN                 DEFAULT TRUE
);

CREATE TABLE PLANES_TIEMPO
(
    ID        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    TENANT_ID UUID         NOT NULL REFERENCES CUENTAS (ID),
    NOMBRE    VARCHAR(100) NOT NULL,
    HORAS     INTEGER      NOT NULL,
    ACTIVO    BOOLEAN          DEFAULT TRUE
);

-- Catálogos físicos: ahora apuntan al SEDE_ID

CREATE TABLE PISOS
(
    ID        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    TENANT_ID UUID        NOT NULL REFERENCES CUENTAS (ID),
    SEDE_ID   UUID        NOT NULL REFERENCES SEDES (ID), 
    NOMBRE    VARCHAR(50) NOT NULL,
    ACTIVO    BOOLEAN          DEFAULT TRUE,
    UNIQUE (SEDE_ID, NOMBRE) 
);

CREATE TABLE HABITACIONES
(
    ID            UUID PRIMARY KEY  DEFAULT gen_random_uuid(),
    TENANT_ID     UUID        NOT NULL REFERENCES CUENTAS (ID),
    SEDE_ID       UUID        NOT NULL REFERENCES SEDES (ID), 
    CATEGORIA_ID  UUID        NOT NULL REFERENCES CATEGORIAS (ID),
    PISO_ID       UUID        NOT NULL REFERENCES PISOS (ID),
    NUMERO        VARCHAR(20) NOT NULL,
    ESTADO_ACTUAL ESTADO_HABITACION DEFAULT 'DISPONIBLE',
    ACTIVO        BOOLEAN           DEFAULT TRUE,
    UNIQUE (SEDE_ID, NUMERO)
);

-- ==========================================
-- 6. MÓDULO DE CLIENTES (DIRECTORIO)
-- ==========================================
-- Clientes globales al TENANT: un huésped puede reservar en cualquier sede

CREATE TABLE CLIENTES
(
    ID               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    TENANT_ID        UUID         NOT NULL REFERENCES CUENTAS (ID),
    NUMERO_DOCUMENTO VARCHAR(20)  NOT NULL,
    NOMBRES          VARCHAR(150) NOT NULL,
    UNIQUE (TENANT_ID, NUMERO_DOCUMENTO)
);

-- ==========================================
-- 7. MÓDULO FINANCIERO Y PROMOCIONES
-- ==========================================
-- Tarifas y promociones globales al TENANT

CREATE TABLE TARIFAS
(
    ID             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    TENANT_ID      UUID           NOT NULL REFERENCES CUENTAS (ID),
    CATEGORIA_ID   UUID           NOT NULL REFERENCES CATEGORIAS (ID),
    PLAN_TIEMPO_ID UUID           NOT NULL REFERENCES PLANES_TIEMPO (ID),
    PRECIO_BASE    DECIMAL(10, 2) NOT NULL,
    ACTIVO         BOOLEAN          DEFAULT TRUE,
    UNIQUE (TENANT_ID, CATEGORIA_ID, PLAN_TIEMPO_ID)
);

CREATE TABLE PROMOCIONES
(
    ID        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    TENANT_ID UUID         NOT NULL REFERENCES CUENTAS (ID),
    NOMBRE    VARCHAR(255) NOT NULL,
    ACTIVO    BOOLEAN          DEFAULT TRUE
);

CREATE TABLE PROMOCION_TARIFAS
(
    ID                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    PROMOCION_ID       UUID           NOT NULL REFERENCES PROMOCIONES (ID),
    TARIFA_ID          UUID           NOT NULL REFERENCES TARIFAS (ID),
    PRECIO_PROMOCIONAL DECIMAL(10, 2) NOT NULL,
    ACTIVO             BOOLEAN          DEFAULT TRUE,
    UNIQUE (PROMOCION_ID, TARIFA_ID)
);

-- ==========================================
-- 8. MÓDULO DE OPERACIONES (CORE)
-- ==========================================

CREATE TABLE RESERVAS
(
    ID                    UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    TENANT_ID             UUID           NOT NULL REFERENCES CUENTAS (ID),
    SEDE_ID               UUID           NOT NULL REFERENCES SEDES (ID),
    HABITACION_ID         UUID           NOT NULL REFERENCES HABITACIONES (ID),
    USUARIO_INGRESO_ID    UUID           NOT NULL REFERENCES USUARIOS (ID),
    CLIENTE_ID            UUID           NOT NULL REFERENCES CLIENTES (ID),
    TARIFA_ID             UUID           NOT NULL REFERENCES TARIFAS (ID),
    PROMOCION_ID          UUID REFERENCES PROMOCIONES (ID),
    CANTIDAD_HUESPEDES    INTEGER                 DEFAULT 1,
    METODO_PAGO           METODO_PAGO    NOT NULL,
    FECHA_INGRESO         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FECHA_SALIDA_ESPERADA TIMESTAMP      NOT NULL,
    FECHA_SALIDA_REAL     TIMESTAMP,
    MONTO_SUBTOTAL        DECIMAL(10, 2) NOT NULL,
    MONTO_DESCUENTO       DECIMAL(10, 2)          DEFAULT 0.00,
    MONTO_PENALIDAD       DECIMAL(10, 2)          DEFAULT 0.00,
    MONTO_TOTAL           DECIMAL(10, 2) NOT NULL
);

-- ==========================================
-- 9. MÓDULO DE AUDITORÍA Y MÉTRICAS (FSM)
-- ==========================================

CREATE TABLE LOG_ESTADOS_HABITACION
(
    ID              UUID PRIMARY KEY           DEFAULT gen_random_uuid(),
    TENANT_ID       UUID              NOT NULL REFERENCES CUENTAS (ID),
    SEDE_ID         UUID              NOT NULL REFERENCES SEDES (ID),
    HABITACION_ID   UUID              NOT NULL REFERENCES HABITACIONES (ID),
    USUARIO_ID      UUID              NOT NULL REFERENCES USUARIOS (ID),
    ESTADO_ANTERIOR ESTADO_HABITACION NOT NULL,
    ESTADO_NUEVO    ESTADO_HABITACION NOT NULL,
    FECHA_CAMBIO    TIMESTAMP         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    OBSERVACION     TEXT
);

-- ==========================================
-- ÍNDICES ESTRATÉGICOS DE RENDIMIENTO
-- ==========================================
CREATE INDEX IDX_SEDES_TENANT               ON SEDES                  (TENANT_ID);
CREATE INDEX IDX_USUARIOS_SEDE              ON USUARIOS               (SEDE_ID);
CREATE INDEX IDX_PISOS_SEDE                 ON PISOS                  (SEDE_ID);
CREATE INDEX IDX_HABITACIONES_SEDE_ESTADO   ON HABITACIONES           (SEDE_ID, ESTADO_ACTUAL);
CREATE INDEX IDX_HABITACIONES_TENANT_ESTADO ON HABITACIONES           (TENANT_ID, ESTADO_ACTUAL); 
CREATE INDEX IDX_HABITACIONES_TENANT_ACTIVO ON HABITACIONES           (TENANT_ID, ACTIVO);
CREATE INDEX IDX_RESERVAS_SEDE              ON RESERVAS               (SEDE_ID, FECHA_INGRESO);
CREATE INDEX IDX_LOG_ESTADOS_METRICA        ON LOG_ESTADOS_HABITACION (TENANT_ID, SEDE_ID, HABITACION_ID, FECHA_CAMBIO);
CREATE INDEX IDX_CLIENTES_DOCUMENTO         ON CLIENTES               (TENANT_ID, NUMERO_DOCUMENTO);