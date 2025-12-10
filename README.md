# 🌿 PlantLight

<p align="center">
  <img src="https://img.shields.io/badge/Android-Kotlin-green?style=for-the-badge&logo=android" alt="Android Kotlin">
  <img src="https://img.shields.io/badge/Jetpack-Compose-blue?style=for-the-badge&logo=jetpackcompose" alt="Jetpack Compose">
  <img src="https://img.shields.io/badge/Architecture-MVVM-orange?style=for-the-badge" alt="MVVM">
</p>

<p align="center">
  <strong>App Android que mide la luz ambiental y te indica si es óptima para tus plantas</strong>
</p>

---

## 📱 Descripción

**PlantLight** es una aplicación Android desarrollada con Kotlin y Jetpack Compose que utiliza el **sensor de luz ambiental** del dispositivo para medir la cantidad de luz (en lux) de tu entorno.

### ¿Qué problema resuelve?
Muchas personas tienen plantas en casa pero no saben si el lugar donde las colocan tiene la cantidad de luz adecuada. PlantLight te ayuda a:

- 📊 **Medir** la luz ambiental en tiempo real
- 🌱 **Comparar** si la luz es óptima para 5 tipos de plantas populares
- 💾 **Guardar** un historial de tus mediciones
- ✅ **Saber** el mejor lugar de tu casa para cada planta

### Sensor Utilizado
🔆 **Sensor de Luz Ambiental** (`Sensor.TYPE_LIGHT`) - Mide la iluminancia en lux

---

## 👨‍💻 Equipo de Desarrollo

| Nombre Completo | GitHub | Rol |
|-----------------|--------|-----|
| [Nombre Integrante 1] | [@usuario1](https://github.com/usuario1) | Sensor de Luz + UI Principal |
| [Nombre Integrante 2] | [@usuario2](https://github.com/usuario2) | API Retrofit + Backend |
| [Nombre Integrante 3] | [@usuario3](https://github.com/usuario3) | Plantas + Navegación |

---

## 🌱 Plantas Soportadas

| Planta | Luz Mínima | Luz Máxima | Tipo |
|--------|------------|------------|------|
| 🪴 Suculenta | 10,000 lux | 50,000 lux | Luz directa |
| 🌿 Pothos | 1,000 lux | 5,000 lux | Luz indirecta |
| 🌾 Helecho | 2,500 lux | 10,000 lux | Luz media |
| 🌵 Sansevieria | 500 lux | 10,000 lux | Baja-media |
| 🌵 Cactus | 15,000 lux | 100,000 lux | Luz intensa |

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Kotlin 100%
- **UI:** Jetpack Compose
- **Arquitectura:** MVVM (Model-View-ViewModel)
- **Networking:** Retrofit 2.9.0
- **API Backend:** MockApi.io
- **Navegación:** Navigation Compose
- **Sensores:** Android Sensor API

---

## 📋 Funcionalidades

- [x] Medición de luz en tiempo real (LUX)
- [x] Lista de 5 plantas con rangos de luz óptima
- [x] Comparación automática luz vs plantas
- [x] Indicador visual de luz óptima/no óptima
- [x] Guardar mediciones en la nube (CRUD)
- [x] Historial de mediciones
- [x] Diseño Material Design 3

### Operaciones CRUD Implementadas
| Operación | Método HTTP | Descripción |
|-----------|-------------|-------------|
| Crear | POST | Guardar nueva medición |
| Leer | GET | Obtener historial de mediciones |
| Actualizar | PUT | Modificar medición existente |
| Eliminar | DELETE | Borrar medición |

---

## 📸 Capturas de Pantalla

> *Próximamente se agregarán capturas de la aplicación funcionando*

| Pantalla Principal | Lista de Plantas | Historial |
|-------------------|------------------|-----------|
| [Screenshot 1] | [Screenshot 2] | [Screenshot 3] |

---

## 🚀 Instalación

### Opción 1: Descargar APK
1. Ir a la sección [Releases](../../releases)
2. Descargar el archivo `PlantLight-v1.0.0.apk`
3. Instalar en tu dispositivo Android

### Opción 2: Compilar desde código
```bash
# Clonar repositorio
git clone https://github.com/USUARIO/IntegradoraDerick.git

# Abrir en Android Studio
# File -> Open -> Seleccionar carpeta

# Ejecutar en emulador o dispositivo
# Click en Run (▶️)
```

---

## 📂 Estructura del Proyecto

```
app/src/main/java/com/example/integradoraderick/
├── data/
│   ├── model/          # Clases de datos (Plant, LightMeasurement)
│   ├── remote/         # Retrofit API Service
│   └── repository/     # Repositorio de datos
├── sensor/             # Manejo del sensor de luz
├── ui/
│   ├── screens/        # Pantallas de la app
│   ├── components/     # Componentes reutilizables
│   └── theme/          # Colores y estilos
└── viewmodel/          # ViewModels (lógica de negocio)
```

---

## 📖 Documentación Adicional

Para más detalles sobre el desarrollo, consultar:
- [Plan de Desarrollo](PLAN_DESARROLLO.md) - Cronograma, división de tareas y guía técnica

---

## 📄 Licencia

Este proyecto fue desarrollado como trabajo académico para [Nombre de la Materia/Universidad].

---

## 🙏 Agradecimientos

- Profesor/a: [Nombre del profesor]
- Materia: [Nombre de la materia]
- Institución: [Nombre de la universidad/escuela]

---

<p align="center">
  Hecho con 💚 y ☕ por el equipo PlantLight
</p>

