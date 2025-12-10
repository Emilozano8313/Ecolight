package com.example.integradoraderick.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.integradoraderick.sensor.LightSensorManager
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel que controla el sensor de luz
 *
 * Usamos AndroidViewModel porque necesitamos el Context de la aplicación
 * para inicializar el sensor de luz
 *
 * @param application Contexto de la aplicación Android
 */
class LightSensorViewModel(application: Application) : AndroidViewModel(application) {

    // Crear instancia del manager del sensor
    private val sensorManager = LightSensorManager(application)

    // Exponer el valor de luz como StateFlow (observable desde Compose)
    val lightLevel: StateFlow<Float> = sensorManager.lightLevel

    // Indica si el sensor está disponible en el dispositivo
    val isSensorAvailable: Boolean = sensorManager.isSensorAvailable

    // Iniciar el sensor cuando se crea el ViewModel
    init {
        sensorManager.startListening()
    }

    /**
     * Se llama automáticamente cuando el ViewModel se destruye
     * Importante para liberar recursos del sensor
     */
    override fun onCleared() {
        super.onCleared()
        sensorManager.stopListening()
    }

    /**
     * Verifica si la luz actual es óptima para una planta
     *
     * @param currentLux Valor actual de luz en lux
     * @param minLux Luz mínima que necesita la planta
     * @param maxLux Luz máxima que tolera la planta
     * @return true si la luz está en el rango óptimo
     */
    fun isLightOptimalForPlant(currentLux: Float, minLux: Int, maxLux: Int): Boolean {
        return currentLux >= minLux && currentLux <= maxLux
    }

    /**
     * Determina la categoría de luz basada en el valor de lux
     * Útil para mostrar información al usuario
     *
     * @param lux Valor de luz en lux
     * @return Descripción de la categoría de luz
     */
    fun getLightCategory(lux: Float): String {
        return when {
            lux < 50 -> "Muy oscuro"
            lux < 500 -> "Luz baja (artificial)"
            lux < 2000 -> "Luz moderada"
            lux < 10000 -> "Luz brillante indirecta"
            lux < 50000 -> "Luz directa"
            else -> "Luz muy intensa"
        }
    }
}

