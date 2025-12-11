package com.example.integradoraderick.data.repository

import com.example.integradoraderick.data.model.LightMeasurement
import com.example.integradoraderick.data.remote.RetrofitClient

/**
 * Repositorio para gestionar las mediciones de luz
 *
 * El patrón Repository actúa como intermediario entre el ViewModel y la fuente de datos.
 * Ventajas:
 * - Centraliza toda la lógica de acceso a datos
 * - Facilita cambiar la fuente de datos (de API a base de datos local, etc.)
 * - Hace el código más testeable
 *
 * Todas las funciones son 'suspend' porque hacen operaciones de red (asíncronas)
 */
class MeasurementRepository {

    // Referencia al servicio de API
    private val apiService = RetrofitClient.apiService

    /**
     * Obtiene todas las mediciones desde la API
     *
     * @return Result con la lista de mediciones o el error
     */
    suspend fun getAllMeasurements(): Result<List<LightMeasurement>> {
        return try {
            val measurements = apiService.getMeasurements()
            Result.success(measurements)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtiene una medición específica por su ID
     *
     * @param id El ID de la medición
     * @return Result con la medición o el error
     */
    suspend fun getMeasurementById(id: String): Result<LightMeasurement> {
        return try {
            val measurement = apiService.getMeasurementById(id)
            Result.success(measurement)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Crea una nueva medición en la API
     *
     * @param measurement Los datos de la nueva medición (sin ID)
     * @return Result con la medición creada (con ID) o el error
     */
    suspend fun createMeasurement(measurement: LightMeasurement): Result<LightMeasurement> {
        return try {
            val createdMeasurement = apiService.createMeasurement(measurement)
            Result.success(createdMeasurement)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza una medición existente
     *
     * @param id El ID de la medición a actualizar
     * @param measurement Los nuevos datos
     * @return Result con la medición actualizada o el error
     */
    suspend fun updateMeasurement(id: String, measurement: LightMeasurement): Result<LightMeasurement> {
        return try {
            val updatedMeasurement = apiService.updateMeasurement(id, measurement)
            Result.success(updatedMeasurement)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina una medición de la API
     *
     * @param id El ID de la medición a eliminar
     * @return Result con la medición eliminada o el error
     */
    suspend fun deleteMeasurement(id: String): Result<LightMeasurement> {
        return try {
            val deletedMeasurement = apiService.deleteMeasurement(id)
            Result.success(deletedMeasurement)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Función auxiliar para guardar una medición de luz rápidamente
     *
     * @param luxValue Valor de luz en lux
     * @param plantName Nombre de la planta
     * @param isOptimal Si la luz es óptima
     * @return Result con la medición creada o el error
     */
    suspend fun saveMeasurement(
        luxValue: Int,
        plantName: String,
        isOptimal: Boolean,
        location: String = ""
    ): Result<LightMeasurement> {
        val measurement = LightMeasurement(
            id = null,  // MockApi genera el ID
            luxValue = luxValue,
            plantName = plantName,
            isOptimal = isOptimal,
            timestamp = java.time.LocalDateTime.now().toString(),
            location = location
        )
        return createMeasurement(measurement)
    }
}

