package com.example.integradoraderick.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integradoraderick.data.model.Plant
import com.example.integradoraderick.data.model.defaultPlants
import com.example.integradoraderick.ui.components.LightMeter
import com.example.integradoraderick.ui.theme.IntegradoraDerickTheme
import com.example.integradoraderick.viewmodel.LightSensorViewModel
import com.example.integradoraderick.viewmodel.PlantViewModel

/**
 * Pantalla principal de la aplicación PlantLight
 * Muestra el medidor de luz en tiempo real
 *
 * @param viewModel ViewModel que controla el sensor de luz
 * @param plantViewModel ViewModel que controla las plantas y mediciones
 * @param modifier Modificador opcional
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: LightSensorViewModel = viewModel(),
    plantViewModel: PlantViewModel = viewModel()
) {
    // Observar el valor de luz del sensor en tiempo real
    val lightLevel by viewModel.lightLevel.collectAsState()

    // Obtener la categoría de luz
    val lightCategory = viewModel.getLightCategory(lightLevel)

    // Estados del PlantViewModel
    val isLoading by plantViewModel.isLoading.collectAsState()
    val successMessage by plantViewModel.successMessage.collectAsState()
    val errorMessage by plantViewModel.errorMessage.collectAsState()
    val plants by plantViewModel.plants.collectAsState()

    // Estado para la planta seleccionada en el dropdown
    var selectedPlant by remember { mutableStateOf<Plant?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Snackbar para mensajes
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar mensajes
    LaunchedEffect(successMessage) {
        successMessage?.let {
            snackbarHostState.showSnackbar(it)
            plantViewModel.clearSuccessMessage()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            plantViewModel.clearError()
        }
    }

    androidx.compose.foundation.layout.Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Header de la app
            Text(
                text = "🌿 PlantLight",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Mide la luz para tus plantas",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Verificar si el sensor está disponible
            if (viewModel.isSensorAvailable) {
                // Componente del medidor de luz
                LightMeter(
                    luxValue = lightLevel,
                    lightCategory = lightCategory
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tarjeta informativa sobre la luz
                LightInfoCard(luxValue = lightLevel)

                Spacer(modifier = Modifier.height(24.dp))

                // ========== SECCIÓN GUARDAR MEDICIÓN ==========
                SaveMeasurementCard(
                    currentLux = lightLevel,
                    plants = plants,
                    selectedPlant = selectedPlant,
                    dropdownExpanded = dropdownExpanded,
                    onPlantSelected = { selectedPlant = it },
                    onDropdownExpandedChange = { dropdownExpanded = it },
                    onSave = {
                        plantViewModel.saveMeasurement(
                            luxValue = lightLevel,
                            plant = selectedPlant
                        )
                    },
                    isLoading = isLoading
                )

            } else {
                // Mostrar mensaje si no hay sensor
                SensorNotAvailableCard()
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta de ayuda con información sobre valores lux
            LuxGuideCard()
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

/**
 * Tarjeta que muestra información sobre el nivel de luz actual
 */
@Composable
private fun LightInfoCard(luxValue: Float) {
    val (plantRecommendation, emoji) = when {
        luxValue < 500 -> "Sansevieria (lengua de suegra)" to "🌵"
        luxValue < 2000 -> "Pothos o plantas de sombra" to "🌿"
        luxValue < 10000 -> "Helechos y la mayoría de plantas de interior" to "🌾"
        luxValue < 50000 -> "Suculentas" to "🪴"
        else -> "Cactus y plantas de sol directo" to "🌵"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$emoji Recomendación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Con esta luz, es ideal para: $plantRecommendation",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Tarjeta que se muestra cuando el sensor de luz no está disponible
 */
@Composable
private fun SensorNotAvailableCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚠ Sensor no disponible",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu dispositivo no tiene sensor de luz ambiental o no está disponible.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

/**
 * Tarjeta guía con información sobre los valores de lux
 */
@Composable
private fun LuxGuideCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "📊 Guía de valores LUX",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LuxGuideItem("0 - 50 lux", "Muy oscuro (noche)", "🌙")
            LuxGuideItem("50 - 500 lux", "Luz artificial", "💡")
            LuxGuideItem("500 - 2,000 lux", "Día nublado", "☁")
            LuxGuideItem("2,000 - 10,000 lux", "Luz indirecta", "🌤")
            LuxGuideItem("10,000 - 50,000 lux", "Sol filtrado", "⛅")
            LuxGuideItem("50,000+ lux", "Sol directo", "☀")
        }
    }
}

/**
 * Item individual de la guía de lux
 */
@Composable
private fun LuxGuideItem(range: String, description: String, emoji: String) {
    Text(
        text = "$emoji $range → $description",
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

/**
 * Tarjeta para guardar una medición de luz
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SaveMeasurementCard(
    currentLux: Float,
    plants: List<Plant>,
    selectedPlant: Plant?,
    dropdownExpanded: Boolean,
    onPlantSelected: (Plant?) -> Unit,
    onDropdownExpandedChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "💾 Guardar Medición",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Selecciona una planta para asociar esta medición:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown para seleccionar planta
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { onDropdownExpandedChange(it) }
            ) {
                OutlinedTextField(
                    value = selectedPlant?.let { "${it.icon} ${it.name}" } ?: "General (sin planta)",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { onDropdownExpandedChange(false) }
                ) {
                    // Opción "General"
                    DropdownMenuItem(
                        text = { Text("☀ General (sin planta específica)") },
                        onClick = {
                            onPlantSelected(null)
                            onDropdownExpandedChange(false)
                        }
                    )

                    // Plantas disponibles
                    plants.forEach { plant ->
                        val isOptimal = plant.isLightOptimal(currentLux)
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${plant.icon} ${plant.name}")
                                    if (isOptimal) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "✓",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            },
                            onClick = {
                                onPlantSelected(plant)
                                onDropdownExpandedChange(false)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar estado de la luz para la planta seleccionada
            selectedPlant?.let { plant ->
                val isOptimal = plant.isLightOptimal(currentLux)
                Text(
                    text = if (isOptimal) "✅ Luz óptima para ${plant.name}" else "⚠ Luz no óptima para ${plant.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isOptimal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botón de guardar
            Button(
                onClick = onSave,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                }
                Text(
                    text = if (isLoading) "Guardando..." else "📥 Guardar ${currentLux.toInt()} lux",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    IntegradoraDerickTheme {
        // En preview no podemos usar el ViewModel real
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌿 PlantLight",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            LightMeter(
                luxValue = 5000f,
                lightCategory = "Luz brillante indirecta"
            )
            Spacer(modifier = Modifier.height(24.dp))
            LuxGuideCard()
            }
        }
}