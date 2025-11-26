package com.chefkiss.app.data.model

data class Review(
    val userName: String,
    val userAvatar: String,
    val rating: Int,
    val comment: String,
    val isNew: Boolean = false // ✅ nuevo campo
)