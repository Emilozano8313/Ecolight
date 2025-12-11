package com.example.integradoraderick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.integradoraderick.ui.theme.IntegradoraDerickTheme

/**
 * Activity principal de la aplicación PlantLight
 *
 * Esta es la entrada principal de la app. Configura el tema
 * y lanza el composable principal con la navegación.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntegradoraDerickTheme {
                // Usar PlantLightApp que contiene toda la navegación
                PlantLightApp()
            }
        }
    }
}
