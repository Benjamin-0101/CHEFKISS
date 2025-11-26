package com.chefkiss.app.data.remote

import com.chefkiss.app.data.model.MealResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TheMealDbApi {
    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse
}

object ExternalRetrofit {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    val api: TheMealDbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMealDbApi::class.java)
    }
}