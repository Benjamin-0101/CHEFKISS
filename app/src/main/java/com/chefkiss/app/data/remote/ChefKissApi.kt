package com.chefkiss.app.data.remote

import com.chefkiss.app.data.model.Restaurant
import com.chefkiss.app.data.model.Review
import com.chefkiss.app.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChefKissApi {

    // --- Auth ---
    @POST("/api/auth/login")
    suspend fun login(@Body user: User): Response<User>

    @POST("/api/auth/register")
    suspend fun register(@Body user: User): Response<User>

    // --- Restaurants ---
    @GET("/api/restaurants")
    suspend fun getAllRestaurants(): List<Restaurant>

    // --- Reviews ---
    @GET("/api/restaurants/{id}/reviews") // Nota: Ajustaremos esto si tu backend no filtra por ID aún
    suspend fun getReviews(): List<Review>
    // Nota: Como tu backend actual devuelve TODAS las reseñas en /api/reviews, usaremos ese por ahora:
    @GET("/api/reviews")
    suspend fun getAllReviews(): List<Review>

    @POST("/api/reviews")
    suspend fun createReview(@Body review: Review): Response<Review>
}