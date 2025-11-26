package com.chefkiss.app.utils

object Validator {

    // Usamos Regex de Kotlin puro en lugar de Patterns de Android
    // para que pase el test unitario en local.
    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
        return email.isNotEmpty() && email.matches(emailRegex)
    }

    fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 6
    }

    fun isReviewValid(comment: String, rating: Int): Boolean {
        return comment.isNotBlank() && rating in 1..5
    }
}