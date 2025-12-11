package com.example.integradoraderick.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.integradoraderick.data.model.Plant
import com.example.integradoraderick.data.model.defaultPlants
import com.example.integradoraderick.ui.theme.IntegradoraDerickTheme

/**
 * Tarjeta que muestra información de una planta y su compatibilidad con la luz actual
 *
 * @param plant La planta a mostrar
 * @param currentLux Valor actual de luz en lux (para mostrar compatibilidad)
 * @param onClick Acción al hacer clic en la tarjeta
 * @param modifier Modificador opcional
 */
@Composable
fun PlantCard(
    plant: Plant,
    currentLux: Float,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Calcular si la luz es óptima y el porcentaje de compatibilidad
    val isOptimal = plant.isLightOptimal(currentLux)
    val matchPercentage = plant.getLightMatchPercentage(currentLux)
    val statusMessage = plant.getLightStatusMessage(currentLux)

    // Animar el color de la tarjeta según la compatibilidad
    val cardColor by animateColorAsState(
        targetValue = when {
            isOptimal -> MaterialTheme.colorScheme.primaryContainer
            matchPercentage > 50 -> MaterialTheme.colorScheme.secondaryContainer
            else -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
        },
        animationSpec = tween(300),
        label = "cardColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icono de la planta
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = plant.icon,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Nombre y nivel de cuidado  :c
                    Column {
                        Text(
                            text = plant.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = plant.careLevel,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                // Indicador de estado
                StatusBadge(isOptimal = isOptimal, statusMessage = statusMessage)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Descripción
            Text(
                text = plant.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            LightRangeInfo(
                minLux = plant.minLux,
                maxLux = plant.maxLux,
                currentLux = currentLux
            )

            Spacer(modifier = Modifier.height(8.dp))

            CompatibilityBar(
                percentage = matchPercentage,
                isOptimal = isOptimal
            )
        }
    }
}


@Composable
private fun StatusBadge(
    isOptimal: Boolean,
    statusMessage: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isOptimal) Color(0xFF4CAF50).copy(alpha = 0.2f)
                else Color(0xFFFF9800).copy(alpha = 0.2f)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isOptimal) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (isOptimal) Color(0xFF4CAF50) else Color(0xFFFF9800),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = if (isOptimal) "Óptimo" else "Ajustar",
            style = MaterialTheme.typography.labelMedium,
            color = if (isOptimal) Color(0xFF4CAF50) else Color(0xFFFF9800),
            fontWeight = FontWeight.Medium
        )
    }
}


 //Muestra información del rango de luz de la planta

@Composable
private fun LightRangeInfo(
    minLux: Int,
    maxLux: Int,
    currentLux: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Rango óptimo",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "${formatLuxValue(minLux)} - ${formatLuxValue(maxLux)} lux",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "Luz actual",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "${formatLuxValue(currentLux.toInt())} lux",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
private fun CompatibilityBar(
    percentage: Int,
    isOptimal: Boolean
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Compatibilidad",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = when {
                    isOptimal -> Color(0xFF4CAF50)
                    percentage > 50 -> Color(0xFFFFA726)
                    else -> Color(0xFFEF5350)
                }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = when {
                isOptimal -> Color(0xFF4CAF50)
                percentage > 50 -> Color(0xFFFFA726)
                else -> Color(0xFFEF5350)
            },
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    }
}


private fun formatLuxValue(lux: Int): String {
    return when {
        lux >= 1000 -> String.format("%.1fk", lux / 1000f)
        else -> lux.toString()
    }
}


 //Vista previa del componente PlantCard
@Preview(showBackground = true)
@Composable
fun PlantCardPreview() {
    IntegradoraDerickTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PlantCard(
                plant = defaultPlants[0], // Suculenta
                currentLux = 15000f // Luz óptima para suculenta
            )
            Spacer(modifier = Modifier.height(16.dp))
            PlantCard(
                plant = defaultPlants[1], // Pothos
                currentLux = 15000f // Demasiada luz para pothos
            )
        }
    }
}

