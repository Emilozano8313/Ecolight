package com.example.integradoraderick.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.integradoraderick.ui.components.LightMeter
import com.example.integradoraderick.ui.theme.IntegradoraDerickTheme
import com.example.integradoraderick.viewmodel.LightSensorViewModel

/**
 * Pantalla principal de la aplicación PlantLight
 * Muestra el medidor de luz en tiempo real
 *
 * @param viewModel ViewModel que controla el sensor de luz
 * @param modifier Modificador opcional
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: LightSensorViewModel = viewModel()
) {
    // Observar el valor de luz del sensor en tiempo real
    val lightLevel by viewModel.lightLevel.collectAsState()

    // Obtener la categoría de luz
    val lightCategory = viewModel.getLightCategory(lightLevel)

    Column(
        modifier = modifier
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

        } else {
            // Mostrar mensaje si no hay sensor
            SensorNotAvailableCard()
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta de ayuda con información sobre valores lux
        LuxGuideCard()
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
                text = "⚠️ Sensor no disponible",
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
            LuxGuideItem("500 - 2,000 lux", "Día nublado", "☁️")
            LuxGuideItem("2,000 - 10,000 lux", "Luz indirecta", "🌤️")
            LuxGuideItem("10,000 - 50,000 lux", "Sol filtrado", "⛅")
            LuxGuideItem("50,000+ lux", "Sol directo", "☀️")
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

