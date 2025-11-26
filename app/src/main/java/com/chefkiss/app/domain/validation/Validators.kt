package com.chefkiss.app.domain.validation

import android.util.Patterns

// #VALIDACION_NOMBRE
fun validateName(name: String): String? {
    if (name.isBlank()) return "El nombre es obligatorio"
    val soloLetras = Regex("^[A-Za-zÁÉÍÓÚÑáéíóúñ ]+$")
    if (!soloLetras.matches(name)) return "Solo letras y espacios"
    return null
}

// #VALIDACION_EMAIL
fun validateEmail(email: String): String? {
    if (email.isBlank()) return "El email es obligatorio"
    val ok = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    if (!ok) return "Formato de email inválido"
    return null
}

// #VALIDACION_TELEFONO
fun validatePhone(phone: String): String? {
    if (phone.isBlank()) return "El teléfono es obligatorio"
    if (!phone.all { it.isDigit() }) return "Solo números"
    if (phone.length !in 8..12) return "Debe tener 8 a 12 dígitos"
    return null
}

// #VALIDACION_PASSWORD
fun validatePassword(pass: String): String? {
    if (pass.isBlank()) return "La contraseña es obligatoria"
    if (pass.length < 8) return "Mínimo 8 caracteres"
    if (!pass.any { it.isUpperCase() }) return "Debe tener 1 mayúscula"
    if (!pass.any { it.isLowerCase() }) return "Debe tener 1 minúscula"
    if (!pass.any { it.isDigit() }) return "Debe tener 1 número"
    if (!pass.any { !it.isLetterOrDigit() }) return "Debe tener 1 símbolo"
    if (pass.contains(' ')) return "Sin espacios"
    return null
}

// #VALIDACION_CONFIRMACION
fun validateConfirm(pass: String, confirm: String): String? {
    if (confirm.isBlank()) return "Confirma tu contraseña"
    if (pass != confirm) return "Las contraseñas no coinciden"
    return null
}
