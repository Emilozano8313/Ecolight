package com.example.integradoraderick.data.model

/**
 * Representa una planta con sus requisitos de luz
 *
 * @param id Identificador único de la planta
 * @param name Nombre común de la planta
 * @param minLux Luz mínima que necesita (en lux)
 * @param maxLux Luz máxima que tolera (en lux)
 * @param description Descripción corta del tipo de luz que prefiere
 * @param icon Emoji representativo de la planta
 * @param careLevel Nivel de dificultad de cuidado
 */

data class Plant(
    val id: Int,
    val name: String,
    val minLux: Int,
    val maxLux: Int,
    val description: String,
    val icon: String,
    val careLevel: String = "Fácil"
) {

    //Verifica si un valor de luz está en el rango óptimo para esta planta
    //currentLux Valor actual de luz en lux
    //@return true si la luz es óptima para esta planta

    fun isLightOptimal(currentLux: Float): Boolean {
        return currentLux >= minLux && currentLux <= maxLux
    }


     //Calcula qué tan cerca está la luz actual del rango óptimo

    // @param currentLux Valor actual de luz en lux
    // @return Un porcentaje de 0 a 100 donde 100 es perfecto

    fun getLightMatchPercentage(currentLux: Float): Int {
        return when {
            currentLux < minLux -> {
                // Luz insuficiente
                val deficit = minLux - currentLux
                val range = minLux.toFloat()
                (100 - (deficit / range * 100)).toInt().coerceIn(0, 100)
            }
            currentLux > maxLux -> {
                // Luz excesiva
                val excess = currentLux - maxLux
                val tolerance = maxLux * 0.5f // 50% de tolerancia extra
                (100 - (excess / tolerance * 100)).toInt().coerceIn(0, 100)
            }
            else -> 100 // Está en el rango óptimo
        }
    }

    /**
     * Obtiene un mensaje descriptivo sobre la luz actual para esta planta
     *
     * @param currentLux Valor actual de luz en lux
     * @return Mensaje descriptivo del estado de la luz
     */
    fun getLightStatusMessage(currentLux: Float): String {
        return when {
            currentLux < minLux -> "Necesita más luz ☁️"
            currentLux > maxLux -> "Demasiada luz ⚠️"
            else -> "¡Luz perfecta! ✨"
        }
    }
}


 //Lista de las 5 plantas predefinidas con sus rangos de luz óptimos

 //Valores de referencia:
 //0-50 lux = Muy oscuro (noche)
 //500 lux = Luz artificial interior
 //500-2,000 lux = Cerca de ventana, día nublado
 //2,000-10,000 lux = Luz indirecta brillante
 //10,000-50,000 lux = Sol directo filtrado
 //50,000+ lux = Sol directo al mediodía

val defaultPlants = listOf(
    Plant(
        id = 1,
        name = "Suculenta",
        minLux = 10000,
        maxLux = 50000,
        description = "Necesita mucha luz directa. Ideal cerca de ventanas soleadas.",
        icon = "🪴",
        careLevel = "Fácil"
    ),
    Plant(
        id = 2,
        name = "Pothos",
        minLux = 1000,
        maxLux = 5000,
        description = "Prefiere luz indirecta. Perfecta para interiores.",
        icon = "🌿",
        careLevel = "Muy fácil"
    ),
    Plant(
        id = 3,
        name = "Helecho",
        minLux = 2500,
        maxLux = 10000,
        description = "Luz media filtrada. Le gusta la humedad.",
        icon = "🌾",
        careLevel = "Moderado"
    ),
    Plant(
        id = 4,
        name = "Sansevieria",
        minLux = 500,
        maxLux = 10000,
        description = "Tolera poca luz. Muy resistente y purifica el aire.",
        icon = "🌵",
        careLevel = "Muy fácil"
    ),
    Plant(
        id = 5,
        name = "Cactus",
        minLux = 15000,
        maxLux = 100000,
        description = "Ama el sol directo. Requiere muy poca agua.",
        icon = "🌵",
        careLevel = "Fácil"
    )
)

