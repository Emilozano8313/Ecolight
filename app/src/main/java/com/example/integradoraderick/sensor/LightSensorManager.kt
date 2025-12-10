package com.example.integradoraderick.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Maneja el sensor de luz del dispositivo
 * Expone los valores a través de StateFlow para usar en Compose
 *
 * @param context Contexto de la aplicación para acceder al servicio de sensores
 */
class LightSensorManager(context: Context) : SensorEventListener {

    // Obtener el servicio de sensores del sistema Android
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Obtener el sensor de luz (puede ser null si el dispositivo no tiene)
    private val lightSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    // StateFlow privado mutable para el valor de luz
    private val _lightLevel = MutableStateFlow(0f)

    // StateFlow público inmutable (solo lectura) para observar desde Compose
    val lightLevel: StateFlow<Float> = _lightLevel.asStateFlow()

    // Indica si el sensor está disponible en el dispositivo
    val isSensorAvailable: Boolean = lightSensor != null

    /**
     * Iniciar a escuchar el sensor de luz
     * Debe llamarse cuando la pantalla se muestra
     */
    fun startListening() {
        lightSensor?.let { sensor ->
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL // Velocidad de actualización normal
            )
        }
    }

    /**
     * Dejar de escuchar el sensor
     * Importante llamar esto para ahorrar batería cuando no se necesita
     */
    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    /**
     * Callback que se llama cuando el sensor detecta un nuevo valor
     *
     * @param event Contiene los valores del sensor
     */
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            // values[0] contiene el valor de luz en lux
            _lightLevel.value = it.values[0]
        }
    }

    /**
     * Callback que se llama cuando cambia la precisión del sensor
     * No lo usamos en esta aplicación
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesitamos hacer nada aquí
    }
}

