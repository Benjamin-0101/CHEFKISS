package com.chefkiss.app.data.model

// La estructura exacta que devuelve TheMealDB
data class MealResponse(
    val meals: List<MealDto>
)

data class MealDto(
    val strMeal: String,      // Nombre de la comida
    val strMealThumb: String  // URL de la foto
)