package com.example.integradoraderick.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.integradoraderick.data.model.LightMeasurement
import com.example.integradoraderick.ui.theme.IntegradoraDerickTheme

@Composable
fun MeasurementItem(
    measurement: LightMeasurement,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información principal
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                OptimalStatusIcon(isOptimal = measurement.isOptimal)

                Spacer(modifier = Modifier.width(12.dp))

                // Detalles de la medición
                Column {
                    // Valor de lux
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = formatLuxValueMeasurement(measurement.luxValue),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "lux",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Planta asociada
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = getPlantEmoji(measurement.plantName),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = measurement.plantName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Fecha y hora
                    Text(
                        text = formatTimestamp(measurement.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )

                    // Ubicación si existe
                    if (measurement.location.isNotBlank()) {
                        Text(
                            text = "📍 ${measurement.location}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            // Botones de acción
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


 //Icono que indica si la medición fue óptima

@Composable
private fun OptimalStatusIcon(isOptimal: Boolean) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(
                if (isOptimal) Color(0xFF4CAF50).copy(alpha = 0.15f)
                else Color(0xFFFF9800).copy(alpha = 0.15f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isOptimal) Icons.Default.Check else Icons.Default.Close,
            contentDescription = if (isOptimal) "Luz óptima" else "Luz no óptima",
            tint = if (isOptimal) Color(0xFF4CAF50) else Color(0xFFFF9800),
            modifier = Modifier.size(24.dp)
        )
    }
}


 //Formatea el valor de lux para mostrar

private fun formatLuxValueMeasurement(lux: Int): String {
    return when {
        lux >= 10000 -> String.format("%.1fk", lux / 1000f)
        lux >= 1000 -> String.format("%.2fk", lux / 1000f)
        else -> lux.toString()
    }
}


 //Formatea el timestamp para mostrar fecha legible

private fun formatTimestamp(timestamp: String): String {
    // El timestamp viene en formato "yyyy-MM-dd HH:mm:ss"
    // Lo convertimos a formato más amigable
    return try {
        val parts = timestamp.split(" ")
        if (parts.size >= 2) {
            val dateParts = parts[0].split("-")
            val timeParts = parts[1].split(":")
            if (dateParts.size >= 3 && timeParts.size >= 2) {
                val day = dateParts[2]
                val month = getMonthName(dateParts[1].toInt())
                val time = "${timeParts[0]}:${timeParts[1]}"
                "$day $month • $time"
            } else {
                timestamp
            }
        } else {
            timestamp
        }
    } catch (e: Exception) {
        timestamp
    }
}

//Obtiene el nombre del mes
private fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "Ene"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Abr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Ago"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dic"
        else -> "---"
    }
}


private fun getPlantEmoji(plantName: String): String {
    return when (plantName.lowercase()) {
        "suculenta" -> "🪴"
        "pothos" -> "🌿"
        "helecho" -> "🌾"
        "sansevieria" -> "🌵"
        "cactus" -> "🌵"
        "general" -> "☀️"
        else -> "🌱"
    }
}


@Composable
fun MeasurementItemCompact(
    measurement: LightMeasurement,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (measurement.isOptimal)
                Color(0xFF4CAF50).copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = getPlantEmoji(measurement.plantName),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "${measurement.luxValue} lux",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = measurement.plantName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                if (measurement.isOptimal) {
                    Text(
                        text = "✓ Óptimo",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = formatTimestamp(measurement.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MeasurementItemPreview() {
    IntegradoraDerickTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            MeasurementItem(
                measurement = LightMeasurement(
                    id = "1",
                    luxValue = 5000,
                    plantName = "Pothos",
                    isOptimal = true,
                    timestamp = "2024-12-10 14:30:00",
                    location = "Sala de estar"
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            MeasurementItem(
                measurement = LightMeasurement(
                    id = "2",
                    luxValue = 50000,
                    plantName = "Cactus",
                    isOptimal = true,
                    timestamp = "2024-12-10 10:15:00"
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            MeasurementItemCompact(
                measurement = LightMeasurement(
                    id = "3",
                    luxValue = 300,
                    plantName = "Helecho",
                    isOptimal = false,
                    timestamp = "2024-12-09 18:00:00"
                )
            )
        }
    }
}

