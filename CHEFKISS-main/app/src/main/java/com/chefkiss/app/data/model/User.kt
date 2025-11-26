package com.chefkiss.app.data.model

data class User(
    val name: String,
    val email: String,
    val phone: String = "",
    val bio: String = ""
)