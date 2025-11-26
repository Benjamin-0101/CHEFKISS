package com.chefkiss.app.data.model

data class User(
    val id: Long? = null,
    val name: String,
    val email: String,
    val password: String? = null,
    val phone: String = "",
    val bio: String = ""
)