📱 Características principales
	•	Registro e inicio de sesión con validación local.
	•	Pantalla principal (Home) con navegación entre secciones.
	•	Publicación de reseñas con cámara integrada.
	•	Perfil de usuario editable con opción de cerrar sesión.
	•	Configuraciones con notificaciones y preferencias.
	•	Asistente culinario simulado (interfaz de IA).
	•	Persistencia local de usuario mediante DataStore.

⸻

🧩 Estructura del proyecto

app/
├── data/
│   ├── local/ (almacenamiento local y DataStore)
│   └── model/ (modelos de datos: User, Review, etc.)
├── ui/
│   ├── screens/ (pantallas Compose: Login, Register, Home, etc.)
│   └── theme/ (colores, tipografía, estilos)
├── viewmodel/
│   └── ChefKissViewModel.kt
└── MainActivity.kt

⸻

⚙️ Tecnologías utilizadas
	•	Lenguaje: Kotlin
	•	Framework UI: Jetpack Compose
	•	Arquitectura: MVVM (Model-View-ViewModel)
	•	Persistencia: Android DataStore
	•	Control de versiones: Git + GitHub

⸻

👨‍💻 Desarrollador

Nombre: Benjamín Espinoza
Carrera: Ingeniería en Informática
Institución: Duoc UC
Asignatura: Desarrollo de Aplicaciones Móviles (DSY1105)
Docente: (Agregar nombre del profesor si lo deseas)

⸻

🚀 Ejecución
	1.	Clonar el repositorio:
git clone https://github.com/Benjamin-0101/CHEFKISS.git
	2.	Abrir el proyecto en Android Studio.
	3.	Ejecutar en un emulador o dispositivo físico con Android 9.0 o superior.
