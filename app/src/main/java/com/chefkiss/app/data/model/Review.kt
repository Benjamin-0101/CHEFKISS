package com.chefkiss.app.data.model

data class Review(
    val id: Long? = null,
    val userName: String,
    val userAvatar: String,
    val rating: Int,
    val comment: String,
    val isNew: Boolean = false,
    val verificationPhotoUrl: String? = null // ✅ Este es el campo que faltaba
)