package com.example.integradoraderick.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integradoraderick.data.model.LightMeasurement
import com.example.integradoraderick.data.model.defaultPlants
import com.example.integradoraderick.ui.components.MeasurementItem
import com.example.integradoraderick.ui.theme.IntegradoraDerickTheme
import com.example.integradoraderick.viewmodel.PlantViewModel

/**
 * Pantalla que muestra el historial de mediciones guardadas
 *
 * @param viewModel ViewModel de plantas y mediciones
 * @param onEditMeasurement Callback cuando se quiere editar una medición
 * @param modifier Modificador opcional
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: PlantViewModel = viewModel(),
    onEditMeasurement: (LightMeasurement) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Estados del ViewModel
    val measurements by viewModel.measurements.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    // Estado para el diálogo de confirmación de eliminación
    var measurementToDelete by remember { mutableStateOf<LightMeasurement?>(null) }

    // Estado para el diálogo de edición
    var measurementToEdit by remember { mutableStateOf<LightMeasurement?>(null) }

    // Snackbar para mensajes
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar mensajes de error o éxito
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    LaunchedEffect(successMessage) {
        successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSuccessMessage()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "📊 Historial",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tus mediciones guardadas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                // Botón de refrescar
                IconButton(onClick = { viewModel.loadMeasurements() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refrescar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Estadísticas
            if (measurements.isNotEmpty()) {
                StatsCard(stats = viewModel.getMeasurementStats())
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Contenido principal
            PullToRefreshBox(
                isRefreshing = isLoading,
                onRefresh = { viewModel.loadMeasurements() },
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    isLoading && measurements.isEmpty() -> {
                        LoadingContent()
                    }
                    measurements.isEmpty() -> {
                        EmptyHistoryContent()
                    }
                    else -> {
                        MeasurementsList(
                            measurements = measurements,
                            onEdit = { measurementToEdit = it },
                            onDelete = { measurementToDelete = it }
                        )
                    }
                }
            }
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Diálogo de confirmación de eliminación
        measurementToDelete?.let { measurement ->
            DeleteConfirmationDialog(
                measurement = measurement,
                onConfirm = {
                    measurement.id?.let { viewModel.deleteMeasurement(it) }
                    measurementToDelete = null
                },
                onDismiss = { measurementToDelete = null }
            )
        }

        // Diálogo de edición
        measurementToEdit?.let { measurement ->
            EditMeasurementDialog(
                measurement = measurement,
                onConfirm = { editedMeasurement ->
                    viewModel.updateMeasurement(editedMeasurement)
                    measurementToEdit = null
                },
                onDismiss = { measurementToEdit = null }
            )
        }
    }
}

/**
 * Tarjeta con estadísticas de las mediciones
 */
@Composable
private fun StatsCard(stats: Map<String, Any>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "📈 Estadísticas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Total",
                    value = "${stats["total"]}",
                    emoji = "📝"
                )
                StatItem(
                    label = "Promedio",
                    value = "${stats["promedio"]} lux",
                    emoji = "📊"
                )
                StatItem(
                    label = "Óptimas",
                    value = "${stats["optimas"]}",
                    emoji = "✅"
                )
            }
        }
    }
}

/**
 * Item individual de estadística
 */
@Composable
private fun StatItem(
    label: String,
    value: String,
    emoji: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = emoji)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
    }
}

/**
 * Lista de mediciones
 */
@Composable
private fun MeasurementsList(
    measurements: List<LightMeasurement>,
    onEdit: (LightMeasurement) -> Unit,
    onDelete: (LightMeasurement) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(
            items = measurements,
            key = { it.id ?: it.timestamp }
        ) { measurement ->
            MeasurementItem(
                measurement = measurement,
                onEdit = { onEdit(measurement) },
                onDelete = { onDelete(measurement) }
            )
        }
    }
}

/**
 * Contenido de carga
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando mediciones...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * Contenido cuando no hay mediciones
 */
@Composable
private fun EmptyHistoryContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No hay mediciones guardadas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ve a la pantalla principal y guarda una medición para verla aquí",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

/**
 * Diálogo de confirmación para eliminar
 */
@Composable
private fun DeleteConfirmationDialog(
    measurement: LightMeasurement,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(text = "¿Eliminar medición?")
        },
        text = {
            Text(
                text = "Se eliminará la medición de ${measurement.luxValue} lux " +
                        "para ${measurement.plantName}. Esta acción no se puede deshacer."
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = "Eliminar",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar")
            }
        }
    )
}

/**
 * Diálogo para editar una medición
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditMeasurementDialog(
    measurement: LightMeasurement,
    onConfirm: (LightMeasurement) -> Unit,
    onDismiss: () -> Unit
) {
    // Estados editables
    var luxValue by remember { mutableStateOf(measurement.luxValue.toString()) }
    var selectedPlantName by remember { mutableStateOf(measurement.plantName) }
    var isOptimal by remember { mutableStateOf(measurement.isOptimal) }
    var location by remember { mutableStateOf(measurement.location) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Lista de plantas disponibles
    val plantOptions = listOf("General") + defaultPlants.map { it.name }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(text = "Editar medición")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Campo de Lux
                OutlinedTextField(
                    value = luxValue,
                    onValueChange = { newValue ->
                        // Solo permitir números
                        if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                            luxValue = newValue
                        }
                    },
                    label = { Text("Valor de luz (lux)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Dropdown de planta
                ExposedDropdownMenuBox(
                    expanded = dropdownExpanded,
                    onExpandedChange = { dropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedPlantName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Planta") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false }
                    ) {
                        plantOptions.forEach { plantName ->
                            val emoji = when (plantName) {
                                "Suculenta" -> "🪴"
                                "Pothos" -> "🌿"
                                "Helecho" -> "🌾"
                                "Sansevieria" -> "🌵"
                                "Cactus" -> "🌵"
                                else -> "☀"
                            }
                            DropdownMenuItem(
                                text = { Text("$emoji $plantName") },
                                onClick = {
                                    selectedPlantName = plantName
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Campo de ubicación
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Ubicación (opcional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Switch de óptimo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "¿Luz óptima?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Switch(
                        checked = isOptimal,
                        onCheckedChange = { isOptimal = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val updatedMeasurement = measurement.copy(
                        luxValue = luxValue.toIntOrNull() ?: measurement.luxValue,
                        plantName = selectedPlantName,
                        isOptimal = isOptimal,
                        location = location
                    )
                    onConfirm(updatedMeasurement)
                },
                enabled = luxValue.isNotEmpty() && luxValue.toIntOrNull() != null
            ) {
                Text(
                    text = "Guardar",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancelar")
            }
        }
    )
}

/**
 * Vista previa de la pantalla
 */
@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    IntegradoraDerickTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "📊 Historial",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Preview con datos de ejemplo
            MeasurementItem(
                measurement = LightMeasurement(
                    id = "1",
                    luxValue = 5000,
                    plantName = "Pothos",
                    isOptimal = true,
                    timestamp = "2024-12-10 14:30:00"
                )
            )
        }
    }
}