# RoomioHub 🏨

¡Bienvenido a **RoomioHub**! Este es un sistema SaaS (Software as a Service) multi-tenant para la gestión hotelera. La plataforma está diseñada bajo un enfoque de **Monolito Modular Pragmático**, lo que permite mantener un estricto aislamiento de los dominios de negocio pero con un flujo de trabajo simplificado (`Controller → Service → Repository`) para maximizar la velocidad de desarrollo del MVP.

---

## 📁 Estructura del Monorepo

El proyecto está organizado en un monorepo para facilitar la gestión tanto del backend como del futuro frontend:

```text
RoomioHub/
├── backend/                # Backend en Spring Boot 4.0.6 (Java 21 + Gradle)
├── frontend/               # (Futuro) Frontend en Angular (Versión por definir)
├── .env.example            # Plantilla de configuración de variables de entorno
├── .env                    # Configuración de entorno local (ignorado en Git)
└── init.sql                # Script DDL de inicialización de la base de datos (PostgreSQL 16)
```

---

## 🛠️ Requisitos Previos

Antes de comenzar, asegúrate de tener instalado lo siguiente en tu máquina de desarrollo:

1. **Java Development Kit (JDK) 21**: Necesario para compilar y ejecutar el backend.
2. **Docker y Docker Compose**: Utilizado para levantar la base de datos local PostgreSQL 16.
3. **Un cliente SQL** (opcional pero recomendado): Herramientas como [DBeaver](https://dbeaver.io/) o [pgAdmin](https://www.pgadmin.org/) para conectarte a la base de datos y ejecutar scripts.
4. **Git**: Para el control de versiones.

---

## 🚀 Pasos para Inicializar el Proyecto

Sigue estos pasos en orden para levantar el entorno de desarrollo localmente:

### 1. Configurar las Variables de Entorno
Copia el archivo de plantilla `.env.example` y crea el archivo `.env` en la raíz del proyecto:

**En Windows (PowerShell):**
```powershell
copy .env.example .env
```

**En Linux / macOS:**
```bash
cp .env.example .env
```

Abre el archivo `.env` recién creado y reemplaza los valores de marcador de posición (`tu_usuario`, `tu_password_seguro`, y `JWT_SECRET_KEY`) con tus credenciales de desarrollo.

### 2. Levantar la Base de Datos con Docker
El backend contiene la configuración de Docker Compose que levanta PostgreSQL 16 y mapea las variables de entorno de tu archivo `.env`.

Dirígete a la carpeta del backend y levanta el contenedor:

```bash
cd backend
docker compose up -d
```
> **Nota:** Esto iniciará la base de datos PostgreSQL en el puerto especificado en tu archivo `.env` (por defecto `5432`).

### 3. Inicializar el Esquema de la Base de Datos
El proyecto utiliza un enfoque de migración manual (`ddl-auto: none`) para mantener control absoluto sobre el esquema. Por ende, debes inicializar la estructura de la base de datos manualmente ejecutando el archivo `init.sql` ubicado en la raíz del monorepo.

Puedes hacerlo de dos formas:
* **Opción A (Recomendado):** Conéctate a la base de datos usando tu cliente SQL favorito (DBeaver, pgAdmin, etc.) con las credenciales que configuraste en tu `.env`, y ejecuta el script completo `init.sql`.
* **Opción B (Línea de comandos):** Si tienes el comando `psql` instalado localmente, puedes ejecutar:
  ```bash
  psql -h localhost -U <tu_usuario> -d roomiohub_db -f ../init.sql
  ```
  *(Reemplaza `<tu_usuario>` con el valor de `POSTGRES_USER` en tu `.env`)*

### 4. Ejecutar el Backend
Una vez que la base de datos esté levantada y el esquema inicializado, puedes iniciar el servidor de desarrollo del backend:

**En Windows (PowerShell / CMD):**
```powershell
./gradlew.bat bootRun
```

**En Linux / macOS:**
```bash
./gradlew bootRun
```

El backend se iniciará y estará escuchando por defecto en el puerto `8080` (o el puerto configurado en `backend/src/main/resources/application.yaml`).

---

## 🔑 Endpoints de Autenticación Disponibles

Actualmente, el backend cuenta con los siguientes endpoints expuestos bajo el prefijo `/api/v1/auth`:

* **Registro de Cuenta y Administrador:** `POST /api/v1/auth/register`
  * Registra una nueva cuenta (Tenant), una sede principal y el usuario administrador de dicha cuenta.
* **Autenticación (Login):** `POST /api/v1/auth/login`
  * Autentica a un usuario y retorna un token JWT válido para las peticiones subsecuentes.
* **Cambio de Contraseña:** `POST /api/v1/auth/change-password`
  * Permite cambiar la contraseña de un usuario autenticado.

---

## 🎨 Frontend (Angular)

El frontend de la aplicación está construido con **Angular 19+** y **Tailwind CSS v4** (con integración del sistema de diseño Material Design 3). Sigue estos pasos para levantarlo:

### 1. Requisitos Previos para el Frontend
* **Node.js** (versión 18 o superior).
* **npm** (usualmente instalado junto con Node.js).

### 2. Instalación de Dependencias
Abre una nueva terminal, dirígete a la carpeta `frontend` y ejecuta:

```bash
cd frontend
npm install
```

### 3. Levantar el Servidor de Desarrollo
Una vez instaladas las dependencias, inicia el servidor de desarrollo de Angular:

```bash
npm start
```

La aplicación web estará disponible en `http://localhost:4200`.

---

## 🚦 Flujo de Desarrollo (Cómo probar el sistema completo)
1. Levanta la Base de Datos: `docker compose up -d` (en `/backend`).
2. Levanta el Backend: `./gradlew bootRun` (en `/backend`).
3. Levanta el Frontend: `npm start` (en `/frontend`).

### 4. Registrar una Cuenta de Prueba
Si no tienes datos iniciales, debes registrar una cuenta enviando una petición al backend. **Importante:** Debes revisar en tu base de datos el UUID de un plan existente para colocarlo en el campo `planId`.

Ejemplo de petición (cURL):
```bash
curl --request POST \
  --url http://localhost:8080/api/v1/auth/register \
  --header 'Content-Type: application/json' \
  --data '{
  "nombreComercial": "Hostal Pro",
  "ruc": "22345678910",
  "username": "HOSTALPRO",
  "planId": "f5e4013b-e199-465b-9a4d-dcdc37a70474"
}'
```

### 5. Acceder a la Aplicación Web
Abre `http://localhost:4200` en tu navegador.

### 6. Iniciar Sesión
Usa las credenciales del usuario que acabas de registrar para iniciar sesión, seleccionar tu sede y acceder al panel principal de gestión hotelera.
