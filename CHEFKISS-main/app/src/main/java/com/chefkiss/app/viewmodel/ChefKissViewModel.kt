package com.chefkiss.app.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chefkiss.app.data.model.Review
import com.chefkiss.app.data.model.User
import com.chefkiss.app.data.local.storage.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChefKissViewModel(context: Context) : ViewModel() {

    private val userPreferences = UserPreferences(context)

    // User state
    val user: StateFlow<User?> = userPreferences.userFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Settings state
    val notificationsEnabled: StateFlow<Boolean> = userPreferences.notificationsEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val emailNotifications: StateFlow<Boolean> = userPreferences.emailNotificationsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    // Reviews state
    private val _reviews = MutableStateFlow(generateMockReviews())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    // Camera image
    private val _cameraImage = MutableStateFlow<Uri?>(null)
    val cameraImage: StateFlow<Uri?> = _cameraImage.asStateFlow()

    // Snackbar message
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // ✅ LOGIN corregido con suspensión
    suspend fun login(email: String, password: String): Pair<Boolean, Map<String, String>> {
        val errors = mutableMapOf<String, String>()

        if (email.isEmpty()) {
            errors["email"] = "Email requerido"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors["email"] = "Email inválido"
        }

        if (password.isEmpty()) {
            errors["password"] = "Contraseña requerida"
        } else if (password.length < 6) {
            errors["password"] = "Mínimo 6 caracteres"
        }

        if (errors.isNotEmpty()) return Pair(false, errors)

        val isValid = userPreferences.isValidLogin(email, password)
        return if (isValid) {
            showMessage("¡Inicio de sesión exitoso!")
            Pair(true, emptyMap())
        } else {
            showMessage("Credenciales inválidas. Debes registrarte primero.")
            Pair(false, mapOf("credentials" to "Credenciales inválidas"))
        }
    }

    // ✅ Registro de usuario
    suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Pair<Boolean, Map<String, String>> {
        val errors = mutableMapOf<String, String>()

        if (name.isEmpty()) errors["name"] = "Nombre requerido"
        if (email.isEmpty()) {
            errors["email"] = "Email requerido"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors["email"] = "Email inválido"
        }
        if (phone.isEmpty()) errors["phone"] = "Teléfono requerido"
        if (password.isEmpty()) errors["password"] = "Contraseña requerida"
        else if (password.length < 6) errors["password"] = "Mínimo 6 caracteres"
        if (confirmPassword.isEmpty()) errors["confirmPassword"] = "Confirma tu contraseña"
        else if (password != confirmPassword) errors["confirmPassword"] = "Las contraseñas no coinciden"

        if (errors.isNotEmpty()) return Pair(false, errors)

        val newUser = User(name = name, email = email, phone = phone)
        userPreferences.saveUser(newUser, password)
        showMessage("¡Bienvenido a ChefKiss, $name! 🎉")

        return Pair(true, emptyMap())
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
            _cameraImage.value = null
            showMessage("Sesión cerrada")
        }
    }

    fun updateProfile(name: String, phone: String, bio: String) {
        if (name.isEmpty()) {
            showMessage("El nombre es requerido")
            return
        }

        viewModelScope.launch {
            user.value?.let {
                val updatedUser = it.copy(name = name, phone = phone, bio = bio)
                userPreferences.updateUser(updatedUser)
                showMessage("Perfil actualizado con éxito")
            }
        }
    }

    fun publishReview(comment: String) {
        if (comment.isEmpty()) {
            showMessage("Por favor escribe tu reseña")
            return
        }

        if (_cameraImage.value == null) {
            showMessage("Primero verifica tu visita con una foto")
            return
        }

        val newReview = Review(
            userName = user.value?.name ?: "Tú",
            userAvatar = "🙋",
            rating = 5,
            comment = comment,
            isNew = true
        )

        _reviews.value = listOf(newReview) + _reviews.value
        showMessage("¡Reseña publicada con éxito!")

        viewModelScope.launch {
            delay(3000)
            _reviews.value = _reviews.value.map { it.copy(isNew = false) }
        }
    }

    fun takePhoto(uri: Uri) {
        _cameraImage.value = uri
        showMessage("¡Foto capturada! Visita verificada.")
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setNotificationsEnabled(enabled)
            showMessage("Configuración guardada")
        }
    }

    fun setEmailNotifications(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setEmailNotifications(enabled)
            showMessage("Configuración guardada")
        }
    }

    fun showMessage(message: String) {
        _snackbarMessage.value = message
    }

    fun clearMessage() {
        _snackbarMessage.value = null
    }

    private fun generateMockReviews(): List<Review> {
        val names = listOf("Ana", "Carlos", "Laura", "Diego", "Sofía", "Mateo", "Valentina", "Sebastián")
        val avatars = listOf("👩", "👨", "👩‍🦰", "👨‍🦱")
        val comments = listOf("Excelente comida", "Muy buena atención", "Delicioso", "Volveré pronto")

        return List(20) { i ->
            Review(
                userName = names[i % names.size],
                userAvatar = avatars[i % avatars.size],
                rating = (4..5).random(),
                comment = comments[i % comments.size]
            )
        }
    }
}