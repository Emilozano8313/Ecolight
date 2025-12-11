package com.example.integradoraderick.data.model

/**
 * Representa una medición de luz guardada en la API
 *
 * Este data class se usa para enviar y recibir datos de MockApi.io
 * Gson (que usa Retrofit) convierte automáticamente JSON <-> Kotlin
 *
 * @param id Identificador único (lo genera MockApi automáticamente)
 * @param luxValue Valor de luz medido en lux
 * @param plantName Nombre de la planta para la que se midió
 * @param isOptimal Si la luz era óptima para esa planta
 * @param timestamp Fecha y hora de la medición (formato ISO)
 * @param location Ubicación donde se tomó la medición (opcional)
 */
data class LightMeasurement(
    val id: String? = null,      // null cuando creamos nuevo, MockApi lo genera
    val luxValue: Int,           // Valor medido en lux
    val plantName: String,       // Para qué planta se midió
    val isOptimal: Boolean,      // ¿Era luz óptima para la planta?
    val timestamp: String,       // Fecha y hora de la medición
    val location: String = ""    // Ubicación opcional
)

