# ChefKiss  - Plataforma de Reseñas Gastronómicas

ChefKiss es una aplicación móvil nativa desarrollada en **Kotlin (Jetpack Compose)** que permite a los usuarios descubrir restaurantes, ver reseñas verificadas y compartir sus propias experiencias gastronómicas validadas mediante fotografía en tiempo real.

El sistema se integra con un Backend de Microservicios en **Spring Boot** y una base de datos **MySQL** desplegada en la nube (Railway).

## 👥 Integrantes
* Bastian David
* (Nombre de tu compañero si tienes)

## 🚀 Funcionalidades Principales
1.  **Autenticación Segura:** Registro e inicio de sesión de usuarios contra base de datos remota.
2.  **Exploración de Restaurantes:** Visualización de locales con detalles, fotos y características.
3.  **Reseñas Verificadas:** Publicación de opiniones (CRUD) que requiere validación fotográfica mediante la cámara del dispositivo.
4.  **API Externa:** Integración con *TheMealDB* para sugerencias de recetas diarias en el Home.
5.  **Modo Offline/Online:** Arquitectura robusta con Retrofit y gestión de estados.

## 🛠️ Stack Tecnológico
* **Frontend:** Android (Kotlin), Jetpack Compose, Retrofit, CameraX, Coil.
* **Backend:** Java 17, Spring Boot 3.4, Spring Data JPA.
* **Base de Datos:** MySQL (Railway).
* **Testing:** JUnit (Pruebas unitarias de validación de lógica).
* **Herramientas:** Android Studio, Visual Studio Code, Postman.

## 🔌 Endpoints Utilizados

### Microservicios Propios (Spring Boot)
* `POST /api/auth/register` - Registro de nuevos usuarios.
* `POST /api/auth/login` - Autenticación de usuarios.
* `GET /api/restaurants` - Obtención del listado de restaurantes.
* `GET /api/reviews` - Obtención de todas las reseñas.
* `POST /api/reviews` - Publicación de una nueva reseña con validación.

### API Externa
* `GET https://www.themealdb.com/api/json/v1/1/random.php` - Obtención de receta aleatoria del día.

## 📲 Pasos para Ejecutar

### Backend
1.  Clonar el repositorio.
2.  Abrir la carpeta `backend` en IntelliJ o VS Code.
3.  Configurar `application.properties` con las credenciales de la base de datos.
4.  Ejecutar `BackendApplication.java`. El servidor iniciará en el puerto `8080` y creará los datos semilla automáticamente (`DataSeeder`).

### Aplicación Móvil
1.  Abrir la carpeta `app` en Android Studio.
2.  Sincronizar el proyecto con Gradle.
3.  Conectar un dispositivo físico o usar un emulador.
4.  Dar clic en "Run". (Asegúrese de que el backend esté corriendo y la IP en `RetrofitClient` sea correcta).

## 📦 Entregables Adjuntos
En la carpeta `/release` se encuentra:
* **APK Firmado:** `app-release.apk`
* **Llave de Firma:** `chefkiss_llave.jks`
