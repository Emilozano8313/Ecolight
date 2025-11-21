# **EcoLight 🌿☀️**

**Tu asistente inteligente para el cuidado de plantas.**

**EcoLight** es una aplicación nativa de Android diseñada para ayudar a los amantes de las plantas a encontrar el lugar perfecto para sus compañeras verdes. Utilizando el sensor de luz ambiental (TYPE\_LIGHT) integrado en los dispositivos móviles, la app mide la iluminancia en tiempo real y determina si las condiciones son óptimas para una especie específica.

## **📱 Capturas de Pantalla (Mockups)**

| Selección de Planta | Medición en Tiempo Real | Resultado / Consejo |
| :---- | :---- | :---- |
| *\[Inserta aquí captura 1\]* | *\[Inserta aquí captura 2\]* | *\[Inserta aquí captura 3\]* |
| *Menú desplegable* | *Lectura de Lux* | *Feedback visual* |

## **✨ Características Principales**

* **Medición en Tiempo Real:** Lectura precisa de la luz ambiental en Lux utilizando el hardware del teléfono.  
* **Base de Datos de Plantas:** Incluye perfiles predefinidos con rangos de luz ideales (Mínimo y Máximo) para plantas comunes como Helechos, Monsteras y Cactus.  
* **Consejero Inteligente:** Sistema de semáforo (Rojo/Verde/Naranja) que indica si la planta necesita más luz, menos luz o si está en su lugar ideal.  
* **Interfaz Intuitiva:** Diseño limpio y fácil de usar, pensado para realizar mediciones rápidas.  
* **Eficiencia Energética:** Gestión optimizada del ciclo de vida del sensor (SensorManager) para no drenar la batería cuando la app no está en uso.

## **🛠️ Tecnologías Utilizadas**

* **Lenguaje:** Kotlin  
* **IDE:** Android Studio (Koala/Jellyfish)  
* **UI:** XML Layouts (View System)  
* **API de Hardware:** android.hardware.Sensor & SensorManager  
* **Versión Mínima SDK:** Android 7.0 (API 24\)

## **🚀 Cómo Empezar**

Sigue estos pasos para clonar y ejecutar el proyecto localmente:

1. **Clonar el repositorio:**  
   git clone \[https://github.com/tu-usuario/EcoLight.git\](https://github.com/tu-usuario/EcoLight.git)

2. **Abrir en Android Studio:**  
   * Abre Android Studio.  
   * Selecciona Open y navega a la carpeta clonada.  
3. **Sincronizar Gradle:**  
   * Espera a que Android Studio descargue las dependencias necesarias.  
4. **Ejecutar:**  
   * Conecta tu dispositivo Android físico (Recomendado, ya que el emulador requiere simulación manual de sensores).  
   * Presiona el botón de Run (▶️).

## **📖 Cómo Usar**

1. Abre la aplicación **EcoLight**.  
2. Selecciona el tipo de planta que deseas ubicar desde el menú desplegable (ej. "Sombra (Helecho)").  
3. Coloca tu teléfono sobre la planta o en el lugar donde planeas ponerla.  
   * *Nota: Asegúrate de que la pantalla mire hacia la fuente de luz (ventana, lámpara).*  
4. Observa el indicador:  
   * 🟢 **Verde:** ¡Lugar perfecto\!  
   * 🔴 **Rojo (Bajo):** Demasiado oscuro. Acerca la planta a la luz.  
   * 🟠 **Naranja (Alto):** Demasiada luz. Busca un lugar con sombra.

## **🔮 Roadmap (Mejoras Futuras)**

* \[ \] Implementar base de datos local con **Room** para agregar más plantas.  
* \[ \] Añadir fotos de referencia para cada planta.  
* \[ \] Algoritmo de promedio de luz (medición durante 5 segundos).  
* \[ \] Historial de mediciones guardadas.

## **🤝 Contribuciones**

¡Las contribuciones son bienvenidas\! Si tienes ideas para mejorar el algoritmo de luz o quieres agregar más plantas a la base de datos:

1. Haz un Fork del proyecto.  
2. Crea una rama para tu característica (git checkout \-b feature/AmazingFeature).  
3. Haz Commit de tus cambios (git commit \-m 'Add some AmazingFeature').  
4. Haz Push a la rama (git push origin feature/AmazingFeature).  
5. Abre un Pull Request.

## **📄 Licencia**

Este proyecto está bajo la Licencia MIT \- mira el archivo LICENSE.md para más detalles.

Hecho con 💚 y ☀️ por \[Tu Nombre\]