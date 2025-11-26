package com.chefkiss.app.data.model

data class Restaurant(
    val id: String,
    val name: String,
    val description: String,
    val address: String,
    val city: String,
    val cuisine: String,
    val priceRange: String,
    val rating: Double,
    val reviewCount: Int,
    val phoneNumber: String,
    val website: String,
    val hours: String,
    val features: List<String>,
    val images: List<String>,
    val menuHighlights: List<MenuItem>
)

data class MenuItem(
    val name: String,
    val description: String,
    val price: String
)
