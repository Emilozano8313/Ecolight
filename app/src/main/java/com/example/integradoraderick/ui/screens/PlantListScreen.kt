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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.integradoraderick.ui.components.PlantCard
import com.example.integradoraderick.ui.theme.IntegradoraDerickTheme
import com.example.integradoraderick.viewmodel.LightSensorViewModel
import com.example.integradoraderick.viewmodel.PlantViewModel

/**
 * Filtros disponibles para ordenar las plantas
 */
enum class PlantFilter(val displayName: String) {
    ALL("Todas"),
    COMPATIBLE("Compatibles"),
    BY_LIGHT("Por luz necesaria")
}

/**
 * Pantalla que muestra la lista de plantas con su compatibilidad de luz
 *
 * @param lightViewModel ViewModel del sensor de luz
 * @param plantViewModel ViewModel de plantas
 * @param onPlantClick Acción al hacer clic en una planta
 * @param modifier Modificador opcional
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantListScreen(
    lightViewModel: LightSensorViewModel = viewModel(),
    plantViewModel: PlantViewModel = viewModel(),
    onPlantClick: (Plant) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Observar el nivel de luz actual
    val currentLux by lightViewModel.lightLevel.collectAsState()
    val plants by plantViewModel.plants.collectAsState()

    // Estado del filtro seleccionado
    var selectedFilter by remember { mutableStateOf(PlantFilter.ALL) }

    // Filtrar y ordenar plantas según el filtro
    val displayedPlants = remember(plants, selectedFilter, currentLux) {
        when (selectedFilter) {
            PlantFilter.ALL -> plants
            PlantFilter.COMPATIBLE -> plants.filter { it.isLightOptimal(currentLux) }
            PlantFilter.BY_LIGHT -> plants.sortedBy { it.minLux }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Header
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "🌱 Mis Plantas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Comprueba qué plantas son ideales para la luz actual",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta de luz actual
        CurrentLightCard(currentLux = currentLux)

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros
        FilterChips(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it },
            compatibleCount = plants.count { it.isLightOptimal(currentLux) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de plantas
        if (displayedPlants.isEmpty()) {
            EmptyPlantsMessage(selectedFilter = selectedFilter)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(
                    items = displayedPlants,
                    key = { it.id }
                ) { plant ->
                    PlantCard(
                        plant = plant,
                        currentLux = currentLux,
                        onClick = {
                            plantViewModel.selectPlant(plant)
                            onPlantClick(plant)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Tarjeta que muestra el nivel de luz actual
 */
@Composable
private fun CurrentLightCard(currentLux: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "☀️ Luz actual",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = formatLuxForDisplay(currentLux),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "lux",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            // Indicador visual
            Text(
                text = getLightEmoji(currentLux),
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}

/**
 * Chips de filtro para las plantas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChips(
    selectedFilter: PlantFilter,
    onFilterSelected: (PlantFilter) -> Unit,
    compatibleCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlantFilter.entries.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = if (filter == PlantFilter.COMPATIBLE)
                            "${filter.displayName} ($compatibleCount)"
                        else
                            filter.displayName
                    )
                }
            )
        }
    }
}

/**
 * Mensaje cuando no hay plantas para mostrar
 */
@Composable
private fun EmptyPlantsMessage(selectedFilter: PlantFilter) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = when (selectedFilter) {
                    PlantFilter.COMPATIBLE -> "No hay plantas compatibles con esta luz"
                    else -> "No hay plantas para mostrar"
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            if (selectedFilter == PlantFilter.COMPATIBLE) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Prueba moverte a un lugar con diferente luz",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
}

/**
 * Formatea el valor de lux para mostrar
 */
private fun formatLuxForDisplay(lux: Float): String {
    return when {
        lux >= 10000 -> String.format("%.1fk", lux / 1000f)
        lux >= 1000 -> String.format("%.2fk", lux / 1000f)
        else -> String.format("%.0f", lux)
    }
}

/**
 * Obtiene un emoji representativo del nivel de luz
 */
private fun getLightEmoji(lux: Float): String {
    return when {
        lux < 50 -> "🌙"
        lux < 500 -> "💡"
        lux < 2000 -> "☁️"
        lux < 10000 -> "🌤️"
        lux < 50000 -> "☀️"
        else -> "🌞"
    }
}

/**
 * Vista previa de la pantalla
 */
@Preview(showBackground = true)
@Composable
fun PlantListScreenPreview() {
    IntegradoraDerickTheme {
        // Preview simplificado con datos estáticos
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "🌱 Mis Plantas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            defaultPlants.take(2).forEach { plant ->
                PlantCard(
                    plant = plant,
                    currentLux = 5000f
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

