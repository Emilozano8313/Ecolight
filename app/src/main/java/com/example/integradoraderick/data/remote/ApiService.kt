package com.example.integradoraderick.data.remote

import com.example.integradoraderick.data.model.LightMeasurement
import retrofit2.http.*

/**
 * Define las llamadas a la API de MockApi.io
 *
 * Retrofit convierte esta interfaz en código real que hace las peticiones HTTP.
 * Cada función representa una operación CRUD (Create, Read, Update, Delete)
 *
 * Las anotaciones (@GET, @POST, etc.) indican el tipo de petición HTTP
 * "suspend" significa que son funciones asíncronas (no bloquean el hilo principal)
 */
interface ApiService {

    /**
     * GET - Obtener todas las mediciones
     *
     * Ejemplo de URL: https://xxxx.mockapi.io/api/v1/measurements
     * Retorna: Lista de todas las mediciones guardadas
     */
    @GET("measurements")
    suspend fun getMeasurements(): List<LightMeasurement>

    /**
     * GET - Obtener una medición específica por ID
     *
     * Ejemplo de URL: https://xxxx.mockapi.io/api/v1/measurements/5
     * @param id El ID de la medición a obtener
     * Retorna: La medición con ese ID
     */
    @GET("measurements/{id}")
    suspend fun getMeasurementById(@Path("id") id: String): LightMeasurement

    /**
     * POST - Crear una nueva medición
     *
     * Ejemplo de URL: https://xxxx.mockapi.io/api/v1/measurements
     * @param measurement Los datos de la nueva medición (sin ID, MockApi lo genera)
     * Retorna: La medición creada (ahora con ID)
     */
    @POST("measurements")
    suspend fun createMeasurement(@Body measurement: LightMeasurement): LightMeasurement

    /**
     * PUT - Actualizar una medición existente
     *
     * Ejemplo de URL: https://xxxx.mockapi.io/api/v1/measurements/5
     * @param id El ID de la medición a actualizar
     * @param measurement Los nuevos datos
     * Retorna: La medición actualizada
     */
    @PUT("measurements/{id}")
    suspend fun updateMeasurement(
        @Path("id") id: String,
        @Body measurement: LightMeasurement
    ): LightMeasurement

    /**
     * DELETE - Eliminar una medición
     *
     * Ejemplo de URL: https://xxxx.mockapi.io/api/v1/measurements/5
     * @param id El ID de la medición a eliminar
     * Retorna: La medición eliminada
     */
    @DELETE("measurements/{id}")
    suspend fun deleteMeasurement(@Path("id") id: String): LightMeasurement
}

