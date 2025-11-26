package com.chefkiss.app.data.local.storage

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.chefkiss.app.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Crea el DataStore a nivel de contexto
val Context.dataStore by preferencesDataStore("chefkiss_prefs")

class UserPreferences(private val context: Context) {

    private object Keys {
        val NAME = stringPreferencesKey("user_name")
        val EMAIL = stringPreferencesKey("user_email")
        val PHONE = stringPreferencesKey("user_phone")
        val PASS = stringPreferencesKey("user_pass")

        val NOTIFICATIONS = booleanPreferencesKey("notifications_enabled")
        val EMAIL_NOTIFICATIONS = booleanPreferencesKey("email_notifications_enabled")
    }

    // 🧑 Usuario actual
    val userFlow: Flow<User?> = context.dataStore.data.map { prefs ->
        val email = prefs[Keys.EMAIL]
        if (email != null) {
            User(
                name = prefs[Keys.NAME] ?: "",
                email = email,
                phone = prefs[Keys.PHONE] ?: ""
            )
        } else null
    }

    // 🔐 Contraseña guardada
    val passFlow: Flow<String?> = context.dataStore.data.map { it[Keys.PASS] }

    // 🔔 Preferencias de notificaciones
    val notificationsEnabledFlow: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.NOTIFICATIONS] ?: true }

    val emailNotificationsFlow: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.EMAIL_NOTIFICATIONS] ?: true }

    // 💾 Guardar usuario
    suspend fun saveUser(user: User, password: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NAME] = user.name
            prefs[Keys.EMAIL] = user.email
            prefs[Keys.PHONE] = user.phone ?: ""
            prefs[Keys.PASS] = password
        }
    }

    // ✏️ Actualizar perfil
    suspend fun updateUser(user: User) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NAME] = user.name
            prefs[Keys.PHONE] = user.phone ?: ""
        }
    }

    // 🚪 Cerrar sesión
    suspend fun logout() {
        context.dataStore.edit { it.clear() }
    }

    // ✅ Alternar notificaciones
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.NOTIFICATIONS] = enabled }
    }

    suspend fun setEmailNotifications(enabled: Boolean) {
        context.dataStore.edit { it[Keys.EMAIL_NOTIFICATIONS] = enabled }
    }

    // 🔑 Validación de login
    suspend fun isValidLogin(email: String, password: String): Boolean {
        val prefs = context.dataStore.data.first()
        val savedEmail = prefs[Keys.EMAIL]
        val savedPass = prefs[Keys.PASS]
        return (savedEmail == email && savedPass == password)
    }
}