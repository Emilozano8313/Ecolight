package com.example.integradoraderick.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.integradoraderick.data.model.LightMeasurement
import com.example.integradoraderick.data.model.Plant
import com.example.integradoraderick.data.model.defaultPlants
import com.example.integradoraderick.data.repository.MeasurementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel que controla las plantas y las mediciones guardadas
 *
 * Este ViewModel maneja:
 * - La lista de plantas disponibles
 * - Las mediciones guardadas en la API
 * - Las operaciones CRUD sobre las mediciones
 */
class PlantViewModel : ViewModel() {

    // Repositorio para acceder a la API
    private val repository = MeasurementRepository()

    // ========== ESTADOS DE PLANTAS ==========

    // Lista de plantas predefinids
    private val _plants = MutableStateFlow(defaultPlants)
    val plants: StateFlow<List<Plant>> = _plants.asStateFlow()

    // Planta seleccionada actualmente
    private val _selectedPlant = MutableStateFlow<Plant?>(null)
    val selectedPlant: StateFlow<Plant?> = _selectedPlant.asStateFlow()

    // ========== ESTADOS DE MEDICIONES ==========

    // Lista de mediciones obtenidas de la API
    private val _measurements = MutableStateFlow<List<LightMeasurement>>(emptyList())
    val measurements: StateFlow<List<LightMeasurement>> = _measurements.asStateFlow()

    // Estado de carga (para mostrar spinner)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        loadMeasurements()
    }

    // ========== FUNCIONES DE PLANTAS ==========


    fun selectPlant(plant: Plant) {
        _selectedPlant.value = plant
    }

    fun clearSelectedPlant() {
        _selectedPlant.value = null
    }


    fun getCompatiblePlants(currentLux: Float): List<Plant> {
        return _plants.value.filter { it.isLightOptimal(currentLux) }
    }


    fun getPlantsSortedByCompatibility(currentLux: Float): List<Plant> {
        return _plants.value.sortedByDescending { it.getLightMatchPercentage(currentLux) }
    }

    // ========== FUNCIONES CRUD DE MEDICIONES ==========

    /**
     * Carga todas las mediciones desde la API
     */
    fun loadMeasurements() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.getAllMeasurements()
                .onSuccess { measurementList ->
                    _measurements.value = measurementList.sortedByDescending { it.timestamp }
                }
                .onFailure { exception ->
                    _errorMessage.value = "Error al cargar mediciones: ${exception.message}"
                }

            _isLoading.value = false
        }
    }


    fun saveMeasurement(luxValue: Float, plant: Plant? = null, location: String = "") {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // Determinar el nombre de la planta y si es óptimo
            val plantName = plant?.name ?: "General"
            val isOptimal = plant?.isLightOptimal(luxValue) ?: false

            // Crear timestamp actual
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(Date())

            val measurement = LightMeasurement(
                id = null, // MockApi lo generará
                luxValue = luxValue.toInt(),
                plantName = plantName,
                isOptimal = isOptimal,
                timestamp = timestamp,
                location = location
            )

            repository.createMeasurement(measurement)
                .onSuccess { createdMeasurement ->
                    _measurements.value = listOf(createdMeasurement) + _measurements.value
                    _successMessage.value = "Medición guardada exitosamente"
                }
                .onFailure { exception ->
                    _errorMessage.value = "Error al guardar: ${exception.message}"
                }

            _isLoading.value = false
        }
    }

    /**
     * Actualiza una medición existente
     *
     * @param measurement La medición con los datos actualizados
     */
    fun updateMeasurement(measurement: LightMeasurement) {
        viewModelScope.launch {
            val id = measurement.id ?: return@launch
            _isLoading.value = true
            _errorMessage.value = null

            repository.updateMeasurement(id, measurement)
                .onSuccess { updatedMeasurement ->
                    // Actualizar en la lista local
                    _measurements.value = _measurements.value.map {
                        if (it.id == id) updatedMeasurement else it
                    }
                    _successMessage.value = "Medición actualizada"
                }
                .onFailure { exception ->
                    _errorMessage.value = "Error al actualizar: ${exception.message}"
                }

            _isLoading.value = false
        }
    }

    /**
     * Elimina una medición
     *
     * @param id El ID de la medición a eliminar
     */
    fun deleteMeasurement(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.deleteMeasurement(id)
                .onSuccess {
                    // Remover de la lista local
                    _measurements.value = _measurements.value.filter { it.id != id }
                    _successMessage.value = "Medición eliminada"
                }
                .onFailure { exception ->
                    _errorMessage.value = "Error al eliminar: ${exception.message}"
                }

            _isLoading.value = false
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Limpia el mensaje de éxito
     */
    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    /**
     * Obtiene las mediciones filtradas por planta
     *
     * @param plantName Nombre de la planta
     * @return Lista de mediciones para esa planta
     */
    fun getMeasurementsByPlant(plantName: String): List<LightMeasurement> {
        return _measurements.value.filter { it.plantName == plantName }
    }

    /**
     * Obtiene estadísticas de las mediciones
     *
     * @return Mapa con estadísticas (promedio, máximo, mínimo, total)
     */
    fun getMeasurementStats(): Map<String, Any> {
        val measurementsList = _measurements.value
        if (measurementsList.isEmpty()) {
            return mapOf(
                "total" to 0,
                "promedio" to 0,
                "maximo" to 0,
                "minimo" to 0,
                "optimas" to 0
            )
        }

        val luxValues = measurementsList.map { it.luxValue }
        return mapOf(
            "total" to measurementsList.size,
            "promedio" to luxValues.average().toInt(),
            "maximo" to (luxValues.maxOrNull() ?: 0),
            "minimo" to (luxValues.minOrNull() ?: 0),
            "optimas" to measurementsList.count { it.isOptimal }
        )
    }
}

