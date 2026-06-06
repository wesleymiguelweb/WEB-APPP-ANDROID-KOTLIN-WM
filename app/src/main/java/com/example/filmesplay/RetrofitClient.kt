package com.example.filmesplay

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Para o emulador padrão do Android Studio use: http://10.0.2.2/api/
    // Para o Genymotion use: http://10.0.3.2/api/ ou o IP da sua máquina
    private const val BASE_URL = "http://10.0.0.104/api/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}