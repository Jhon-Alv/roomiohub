# RoomioHub 🏨

¡Bienvenido a **RoomioHub**! Este es un sistema SaaS (Software as a Service) multi-tenant para la gestión hotelera. La plataforma está diseñada bajo un enfoque de **Monolito Modular Pragmático**, lo que permite mantener un estricto aislamiento de los dominios de negocio pero con un flujo de trabajo simplificado (`Controller → Service → Repository`) para maximizar la velocidad de desarrollo del MVP.

---

## 📁 Estructura del Monorepo

El proyecto está organizado en un monorepo para facilitar la gestión tanto del backend como del frontend:

```text
RoomioHub/
├── backend/                # Backend en Spring Boot 4.0.6 (Java 21 + Gradle)
│   ├── .env.example        # Plantilla de configuración de entorno del backend
│   └── .env                # Variables de entorno locales del backend (ignorado en Git)
├── frontend/               # Frontend en Angular 19+ (Tailwind CSS v4)
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

### 1. Configurar las Variables de Entorno del Backend
Cada componente gestiona sus propias configuraciones locales. Dirígete a la carpeta `backend/` y crea su archivo `.env`:

**En Windows (PowerShell):**
```powershell
cd backend
copy .env.example .env
```

**En Linux / macOS:**
```bash
cd backend
cp .env.example .env
```

Abre `backend/.env` y personaliza sus valores.

#### ⚠️ Resolución de Conflictos de Puerto de Base de Datos
Si ya tienes una instancia de PostgreSQL ejecutándose nativamente en tu máquina en el puerto por defecto (`5432`), el contenedor de Docker Compose no podrá enlazarse a dicho puerto.
Para solucionarlo:
1. En `backend/.env`, cambia `POSTGRES_PORT` a `5433`:
   ```env
   POSTGRES_PORT=5433
   ```
2. Modifica la URL de conexión del datasource (`BD_ROOMIOHUB_URL`) para que apunte al puerto `5433`:
   ```env
   BD_ROOMIOHUB_URL=jdbc:postgresql://localhost:5433/roomiohub_db?timezone=America/Lima
   ```

*(Si no tienes PostgreSQL corriendo localmente, puedes mantener el puerto por defecto `5432`)*.

---

### 2. Levantar la Base de Datos con Docker
El archivo `docker-compose.yaml` está configurado para leer directamente las variables de entorno de tu archivo `backend/.env` local.

Dirígete a la carpeta del backend (si no estás ahí) y levanta el contenedor:

```bash
cd backend
docker compose up -d
```
> **Nota:** Esto iniciará el contenedor de PostgreSQL en segundo plano y expondrá la base de datos en el puerto indicado en tu `POSTGRES_PORT` (por defecto `5432` o `5433` si lo modificaste).

---

### 3. Inicializar el Esquema de la Base de Datos
El proyecto utiliza un enfoque de migración manual (`ddl-auto: none`) para mantener control absoluto sobre el esquema. Debes inicializar la estructura de la base de datos ejecutando el archivo `init.sql` ubicado en la raíz del monorepo.

* **Opción A (Recomendado):** Conéctate a la base de datos usando tu cliente SQL favorito (DBeaver, pgAdmin, etc.) utilizando `localhost`, el puerto configurado en tu `.env` (`5432` o `5433`) y tus credenciales, y ejecuta el script `init.sql`.
* **Opción B (Línea de comandos):** Si tienes `psql` instalado localmente, puedes ejecutar:
  ```bash
  # Desde la carpeta backend/
  psql -h localhost -p <puerto> -U <tu_usuario> -d roomiohub_db -f ../init.sql
  ```
  *(Reemplaza `<puerto>` y `<tu_usuario>` con los valores correspondientes de tu `.env`)*

---

### 4. Ejecutar el Backend desde el IDE (Recomendado: IntelliJ IDEA)

Para asegurar que las credenciales no se utilicen con valores por defecto débiles en desarrollo ni en producción, **se han removido los valores de fallback de la configuración**. El backend requiere obligatoriamente que configures tu archivo `.env` en la carpeta `backend/` para iniciarse.

Dado que en desarrollo es común ejecutar el backend directamente desde el IDE, sigue estos pasos para configurar la carga del archivo `.env` en **IntelliJ IDEA**:

1. Abre el proyecto en **IntelliJ IDEA**.
2. Ve a la parte superior derecha y haz clic en el menú desplegable junto al botón verde de "Run" (Reproducir) y selecciona **Edit Configurations...**.
3. Selecciona la configuración de ejecución de Spring Boot de tu proyecto (usualmente llamada `BackendApplication` o similar).
4. Configura la carga del `.env`:
   * **Opción A (Nativo - Recomendado)**: En las versiones recientes de IntelliJ IDEA, puedes cargar un archivo `.env` directamente sin usar plugins:
     * Ve al campo **Environment variables** y haz clic en el icono de edición (el lápiz/cuadrado a la derecha) para abrir la ventana de variables.
     * En la sección inferior llamada **Env files** (o mediante la carpeta de archivos), haz clic en el botón `+` (Add) y selecciona el archivo `backend/.env` que creaste.
     * Guarda y confirma para que el IDE lea las variables del archivo automáticamente.
   * **Opción B (Entrada Manual)**: Si prefieres no usar la opción de archivo o tienes una versión muy antigua:
     * En el campo **Environment variables**, escribe o pega tus variables en formato `KEY=VALUE` (separadas por punto y coma `;`).
5. Aplica los cambios y ejecuta la aplicación haciendo clic en el botón de **Run** (Reproducir).

El backend iniciará correctamente y escuchará peticiones en el puerto `8080`.

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
