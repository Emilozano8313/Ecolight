package com.example.integradoraderick.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.integradoraderick.ui.theme.IntegradoraDerickTheme
import java.util.Locale

/**
 * Componente visual que muestra el medidor de luz en forma de arco
 *
 * @param luxValue Valor actual de luz en lux
 * @param lightCategory Categoría descriptiva de la luz
 * @param modifier Modificador opcional para el componente
 */
@Composable
fun LightMeter(
    luxValue: Float,
    lightCategory: String,
    modifier: Modifier = Modifier
) {
    // Normalizar el valor de lux para el arco (0 a 1)
    // Usamos 100,000 lux como máximo (sol directo al mediodía)
    val normalizedValue = (luxValue / 100000f).coerceIn(0f, 1f)

    // Animación suave del valor
    val animatedProgress by animateFloatAsState(
        targetValue = normalizedValue,
        animationSpec = tween(durationMillis = 300),
        label = "progress"
    )

    // Determinar el color basado en el nivel de luz
    val meterColor = when {
        luxValue < 500 -> Color(0xFF5C6BC0)      // Azul - muy oscuro
        luxValue < 2000 -> Color(0xFF26A69A)     // Verde azulado - bajo
        luxValue < 10000 -> Color(0xFF66BB6A)    // Verde - óptimo para muchas plantas
        luxValue < 50000 -> Color(0xFFFFA726)    // Naranja - brillante
        else -> Color(0xFFEF5350)                 // Rojo - muy intenso
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título del medidor
            Text(
                text = "☀️ Sensor de Luz",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Arco del medidor
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(180.dp)) {
                    val strokeWidth = 20.dp.toPx()
                    val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                    val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

                    // Arco de fondo (gris)
                    drawArc(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Arco de progreso (coloreado)
                    drawArc(
                        color = meterColor,
                        startAngle = 135f,
                        sweepAngle = 270f * animatedProgress,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Valor de lux en el centro
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = formatLuxValue(luxValue),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = meterColor
                    )
                    Text(
                        text = "lux",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Categoría de luz
            Text(
                text = lightCategory,
                style = MaterialTheme.typography.titleMedium,
                color = meterColor,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción de rangos
            Text(
                text = getLightDescription(luxValue),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Formatea el valor de lux para mostrar de forma legible
 */
private fun formatLuxValue(lux: Float): String {
    return when {
        lux >= 10000 -> String.format(Locale.US, "%.1fk", lux / 1000)
        lux >= 1000 -> String.format(Locale.US, "%.0f", lux)
        else -> String.format(Locale.US, "%.0f", lux)
    }
}

/**
 * Obtiene una descripción del tipo de luz actual
 */
private fun getLightDescription(lux: Float): String {
    return when {
        lux < 50 -> "Condiciones de noche o habitación muy oscura"
        lux < 500 -> "Similar a luz artificial de interiores"
        lux < 2000 -> "Como cerca de una ventana en día nublado"
        lux < 10000 -> "Buena luz indirecta, ideal para muchas plantas"
        lux < 50000 -> "Luz solar directa filtrada"
        else -> "Sol directo muy intenso"
    }
}

@Preview(showBackground = true)
@Composable
fun LightMeterPreview() {
    IntegradoraDerickTheme {
        LightMeter(
            luxValue = 5000f,
            lightCategory = "Luz brillante indirecta",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LightMeterLowLightPreview() {
    IntegradoraDerickTheme {
        LightMeter(
            luxValue = 200f,
            lightCategory = "Luz baja",
            modifier = Modifier.padding(16.dp)
        )
    }
}

