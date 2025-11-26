package com.chefkiss.app.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // 10.0.2.2 es la dirección especial para que el Emulador vea tu PC (localhost)
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val api: ChefKissApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChefKissApi::class.java)
    }
}