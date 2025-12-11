package com.example.integradoraderick

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.integradoraderick.ui.screens.HistoryScreen
import com.example.integradoraderick.ui.screens.HomeScreen
import com.example.integradoraderick.ui.screens.PlantListScreen
import com.example.integradoraderick.viewmodel.LightSensorViewModel
import com.example.integradoraderick.viewmodel.PlantViewModel

/**
 * Rutas de navegación de la aplicación
 */
sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : Screen(
        route = "home",
        title = "Inicio",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Plants : Screen(
        route = "plants",
        title = "Plantas",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )

    data object History : Screen(
        route = "history",
        title = "Historial",
        selectedIcon = Icons.Filled.DateRange,
        unselectedIcon = Icons.Outlined.DateRange
    )
}

/**
 * Lista de pantallas para la barra de navegación
 */
val bottomNavItems = listOf(
    Screen.Home,
    Screen.Plants,
    Screen.History
)

/**
 * Composable principal de la aplicación
 * Maneja la navegación entre pantallas
 *
 * @param navController Controlador de navegación
 * @param lightViewModel ViewModel compartido del sensor de luz
 * @param plantViewModel ViewModel compartido de plantas
 */
@Composable
fun PlantLightApp(
    navController: NavHostController = rememberNavController(),
    lightViewModel: LightSensorViewModel = viewModel(),
    plantViewModel: PlantViewModel = viewModel()
) {
    Scaffold(
        bottomBar = {
            PlantLightBottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Pantalla de inicio (medidor de luz)
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = lightViewModel
                )
            }

            // Pantalla de lista de plantas
            composable(Screen.Plants.route) {
                PlantListScreen(
                    lightViewModel = lightViewModel,
                    plantViewModel = plantViewModel,
                    onPlantClick = { plant ->
                        // Aquí podrías navegar a una pantalla de detalle
                        // Por ahora solo seleccionamos la planta
                        plantViewModel.selectPlant(plant)
                    }
                )
            }

            // Pantalla de historial
            composable(Screen.History.route) {
                HistoryScreen(
                    viewModel = plantViewModel,
                    onEditMeasurement = { measurement ->
                        // Aquí podrías abrir un diálogo de edición
                        // Por ahora solo imprimimos
                    }
                )
            }
        }
    }
}

/**
 * Barra de navegación inferior
 */
@Composable
private fun PlantLightBottomNavigation(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == screen.route
            } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                        contentDescription = screen.title
                    )
                },
                label = { Text(text = screen.title) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop hasta el destino inicial para evitar acumulación
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Evitar múltiples copias del mismo destino
                        launchSingleTop = true
                        // Restaurar estado al reseleccionar
                        restoreState = true
                    }
                }
            )
        }
    }
}

