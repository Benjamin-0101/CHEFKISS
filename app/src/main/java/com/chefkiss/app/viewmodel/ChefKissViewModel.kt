package com.chefkiss.app.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chefkiss.app.data.model.Restaurant
import com.chefkiss.app.data.model.Review
import com.chefkiss.app.data.model.User
import com.chefkiss.app.data.remote.RetrofitClient
import com.chefkiss.app.data.remote.ExternalRetrofit
import com.chefkiss.app.data.local.storage.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChefKissViewModel(context: Context) : ViewModel() {

    private val userPreferences = UserPreferences(context)

    val user: StateFlow<User?> = userPreferences.userFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val notificationsEnabled: StateFlow<Boolean> = userPreferences.notificationsEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val emailNotifications: StateFlow<Boolean> = userPreferences.emailNotificationsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants.asStateFlow()

    private val _dailyMeal = MutableStateFlow<com.chefkiss.app.data.model.MealDto?>(null)
    val dailyMeal: StateFlow<com.chefkiss.app.data.model.MealDto?> = _dailyMeal.asStateFlow()

    private val _cameraImage = MutableStateFlow<Uri?>(null)
    val cameraImage: StateFlow<Uri?> = _cameraImage.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                val remoteRestaurants = RetrofitClient.api.getAllRestaurants()
                _restaurants.value = remoteRestaurants

                val remoteReviews = RetrofitClient.api.getAllReviews()
                _reviews.value = remoteReviews

                val mealResponse = ExternalRetrofit.api.getRandomMeal()
                _dailyMeal.value = mealResponse.meals.firstOrNull()

            } catch (e: Exception) {
                Log.e("API_ERROR", "Error al cargar datos: ${e.message}")
            }
        }
    }

    suspend fun login(email: String, password: String): Pair<Boolean, Map<String, String>> {
        val errors = mutableMapOf<String, String>()
        if (email.isEmpty()) errors["email"] = "Requerido"
        if (password.isEmpty()) errors["password"] = "Requerido"
        if (errors.isNotEmpty()) return Pair(false, errors)

        return try {
            val loginUser = User(name = "", email = email, password = password)
            val response = RetrofitClient.api.login(loginUser)

            if (response.isSuccessful && response.body() != null) {
                val backendUser = response.body()!!
                userPreferences.saveUser(backendUser, password)
                showMessage("¡Bienvenido de nuevo, ${backendUser.name}!")
                Pair(true, emptyMap())
            } else {
                showMessage("Credenciales incorrectas")
                Pair(false, mapOf("credentials" to "Email o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Log.e("LOGIN_ERROR", "Fallo al conectar: ${e.message}", e)
            showMessage("Error de conexión con el servidor")
            Pair(false, mapOf("connection" to "Error de red"))
        }
    }

    suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Pair<Boolean, Map<String, String>> {
        val errors = mutableMapOf<String, String>()
        if (password != confirmPassword) errors["confirmPassword"] = "No coinciden"
        if (errors.isNotEmpty()) return Pair(false, errors)

        return try {
            val newUser = User(name = name, email = email, phone = phone, password = password)
            val response = RetrofitClient.api.register(newUser)

            if (response.isSuccessful) {
                showMessage("¡Cuenta creada en la nube! Inicia sesión.")
                Pair(true, emptyMap())
            } else {
                showMessage("No se pudo registrar (Email ya existe)")
                Pair(false, mapOf("email" to "Error en registro"))
            }
        } catch (e: Exception) {
            Log.e("ERROR_REGISTRO", "El celular falló al conectar: ${e.message}", e)
            showMessage("Error de conexión: Revisa tu internet o IP")
            Pair(false, mapOf("connection" to "Error de red"))
        }
    }

    fun publishReview(comment: String) {
        if (comment.isEmpty()) {
            showMessage("Escribe un comentario")
            return
        }
        if (_cameraImage.value == null) {
            showMessage("Primero verifica tu visita con una foto")
            return
        }

        viewModelScope.launch {
            try {
                val currentUser = user.value
                val newReview = Review(
                    userName = currentUser?.name ?: "Anónimo",
                    userAvatar = "👤",
                    rating = 5,
                    comment = comment,
                    isNew = true,
                    verificationPhotoUrl = _cameraImage.value.toString()
                )

                val response = RetrofitClient.api.createReview(newReview)

                if (response.isSuccessful) {
                    showMessage("¡Reseña publicada y guardada!")
                    loadData()
                } else {
                    showMessage("Error al guardar reseña")
                }
            } catch (e: Exception) {
                showMessage("Error de red al publicar")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
            _cameraImage.value = null
            showMessage("Sesión cerrada")
        }
    }

    fun updateProfile(name: String, phone: String, bio: String) {
        viewModelScope.launch {
            user.value?.let {
                val updatedUser = it.copy(name = name, phone = phone, bio = bio)
                userPreferences.updateUser(updatedUser)
                showMessage("Perfil actualizado")
            }
        }
    }

    fun takePhoto(uri: Uri) {
        _cameraImage.value = uri
        showMessage("Foto capturada")
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
}