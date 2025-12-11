package com.example.integradoraderick.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Retrofit configurado para conectar con MockApi.io
 *
 * Este objeto (singleton) crea y configura Retrofit una sola vez.
 * Usamos 'object' en Kotlin para crear un singleton (una única instancia).
 *
 * Retrofit es una librería que facilita hacer peticiones HTTP a APIs REST.
 * Gson es una librería que convierte objetos Kotlin <-> JSON automáticamente.
 */
object RetrofitClient {

    /**
     * ⚠️ IMPORTANTE: Cambia esta URL por la de tu proyecto en MockApi.io
     *
     * Para obtener tu URL:
     * 1. Ve a mockapi.io y crea una cuenta
     * 2. Crea un nuevo proyecto llamado "PlantLight"
     * 3. Crea un recurso llamado "measurements"
     * 4. Copia la URL base que te da MockApi
     *
     * La URL debe terminar con "/" (barra diagonal)
     */
    private const val BASE_URL = "https://693a225bc8d59937aa09e48e.mockapi.io/api/v1/"

    /**
     * Instancia de Retrofit configurada
     *
     * 'by lazy' significa que se crea solo cuando se usa por primera vez
     * Esto es eficiente porque no se crea si no se necesita
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)                              // URL base de la API
            .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON <-> Kotlin
            .build()
    }

    /**
     * Instancia del servicio API lista para usar
     *
     * Retrofit toma la interfaz ApiService y genera el código
     * real que hace las peticiones HTTP
     *
     * Uso: RetrofitClient.apiService.getMeasurements()
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

