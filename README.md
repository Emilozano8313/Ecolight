# PlantLight

> *Proyecto Integrador - Desarrollo de Aplicaciones Móviles*
>
> *Semestre:* [4-F]
> *Fecha de entrega:* 11 de Diciembre de 2025

---

## Equipo de Desarrollo

| Nombre Completo                    | Rol / Tareas Principales | Usuario GitHub |
|:-----------------------------------| :--- |:---------------|
| [Benitez Castellanos Diego Javier] | Sensor de Luz + Pantalla Principal | @SkyTotix      |
| [Lozano Villafaña Emiliano]        | API Retrofit + MockApi.io | @Emilozano8313 |
| [Reyes Recillas Diego Emilinano]   | Plantas + Navegacion + Historial | @Sundown-uwu   |

---

## Descripcion del Proyecto

*Que hace la aplicacion?*

PlantLight es una aplicacion que utiliza el sensor de luz ambiental del celular para medir la cantidad de luz (en lux) de cualquier espacio. La app compara esta medicion con los rangos optimos de luz de 5 plantas populares de interior, indicandote si el lugar es adecuado para cada una de ellas.

*Problema que resuelve:* Muchas personas tienen plantas en casa pero no saben si el lugar donde las colocan tiene la cantidad de luz adecuada. PlantLight te ayuda a encontrar el mejor lugar de tu casa para cada tipo de planta.

*Dirigida a:* Personas que tienen plantas de interior y quieren asegurarse de que reciban la luz adecuada para su crecimiento.

*Funcionalidad principal:* Medir luz en tiempo real, comparar con 5 plantas predefinidas, y guardar un historial de mediciones en la nube.

*Objetivo:*
Demostrar la implementacion de una arquitectura robusta en Android utilizando servicios web y hardware del dispositivo.

---

## Stack Tecnologico y Caracteristicas

Este proyecto ha sido desarrollado siguiendo estrictamente los lineamientos de la materia:

* *Lenguaje:* Kotlin 100%.
* *Interfaz de Usuario:* Jetpack Compose.
* *Arquitectura:* MVVM (Model-View-ViewModel).
* *Conectividad (API REST):* Retrofit.
    * *GET:* Obtiene todas las mediciones guardadas del historial desde MockApi.io
    * *POST:* Crea una nueva medicion de luz con la planta asociada y guarda en la nube
    * *UPDATE:* Permite editar una medicion existente (valor de lux, planta, ubicacion)
    * *DELETE:* Elimina una medicion del historial
* *Sensor Integrado:* Sensor de Luz Ambiental (Sensor.TYPE_LIGHT)
    * Uso: Mide la iluminancia en lux del ambiente en tiempo real para determinar si la luz es optima para las diferentes plantas

---

## Plantas Soportadas

| Planta | Luz Minima | Luz Maxima | Tipo de Luz |
| :--- | :---: | :---: | :--- |
| Suculenta | 10,000 lux | 50,000 lux | Luz directa fuerte |
| Pothos | 1,000 lux | 5,000 lux | Luz indirecta |
| Helecho | 2,500 lux | 10,000 lux | Luz media filtrada |
| Sansevieria | 500 lux | 10,000 lux | Baja a media |
| Cactus | 15,000 lux | 100,000 lux | Luz muy intensa |

---

## Instalacion y Releases

El ejecutable firmado (.apk) se encuentra disponible en la seccion de *Releases* de este repositorio.

### Descargar APK

1. Ve a la seccion "Releases" (o haz clic [aqui](../../releases)).
2. Descarga el archivo .apk de la ultima version.
3. Instalalo en tu dispositivo Android (asegurate de permitir la instalacion de origenes desconocidos)

## Configuracion de la API (MockApi.io)

La aplicacion utiliza MockApi.io como backend. La URL base configurada es:


https://693a225bc8d59937aa09e48e.mockapi.io/


El recurso measurements tiene la siguiente estructura:

| Campo | Tipo | Descripcion |
| :--- | :--- | :--- |
| id | String | ID unico (auto-generado) |
| luxValue | Number | Valor de luz en lux |
| plantName | String | Nombre de la planta |
| isOptimal | Boolean | Si la luz era optima |
| timestamp | String | Fecha y hora |
| location | String | Ubicacion (opcional) |

---

## Licencia

Este proyecto fue desarrollado como trabajo academico para la materia de Desarrollo de Aplicaciones Moviles.

---

## Agradecimientos

- *Profesor/a:* Derick Aaxel Lagunes Ramirez
- *Materia:* Desarrollo de Aplicaciones Moviles
- *Institucion:* Universidad Tecnologica Emiliano Zapata