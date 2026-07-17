# Registro de Empleados

Aplicación Android desarrollada en Kotlin con Jetpack Compose para el registro de empleados. Permite cargar empleados mediante un formulario, verlos en una lista vertical y eliminarlos de forma individual.

Alumno: Miguel Ángel Jara Acuña
Curso: 232 - INTRODUCCION A LA PROGRAMACION PARA DISPOSITIVOS MOVILES

## Estructura del proyecto

El código fuente está en `app/src/main/java/com/uaa/registroempleados/` y se organiza así:

`MainActivity.kt` contiene la Activity principal y todos los composables de la pantalla. La Activity sobrescribe los métodos `onStart()`, `onStop()` y `onDestroy()`, y en cada uno registra un mensaje con `Log.i(TAG, "nombre del método")`. La pantalla se compone de un formulario de ingreso (`FormularioEmpleado`), la lista de empleados implementada con `LazyColumn` y el ítem de cada empleado (`EmpleadoItem`), que muestra el nombre destacado arriba, un `LazyRow` con el cargo, departamento, salario y fecha de contratación, y un botón Eliminar en la parte inferior.

`model/Empleado.kt` define la data class con los datos del empleado: nombre completo, cargo, departamento, salario y fecha de contratación.

`ui/theme/` contiene el tema personalizado generado con Material Design Theme Builder. `Color.kt` define la paleta (color semilla #00696D), `Theme.kt` arma los esquemas claro y oscuro con `lightColorScheme` y `darkColorScheme`, y `Type.kt` define la tipografía. El tema cambia automáticamente entre modo claro y oscuro según la configuración del sistema, usando `isSystemInDarkTheme()`.

La lista de empleados se mantiene en memoria con una lista de estado de Compose, por lo que la interfaz se actualiza sola al agregar o eliminar registros.

## Como ejecutar la aplicación

1. Abrir el proyecto en Android Studio (File, Open, seleccionar la carpeta del proyecto) y esperar a que termine la sincronización de Gradle.
2. Ejecutar la app con el botón Run sobre un emulador o un dispositivo físico con Android 7.0 (API 24) o superior.
3. Completar los campos del formulario y presionar Registrar empleado. El empleado aparece en la lista debajo del formulario y se puede quitar con el botón Eliminar de cada ítem.

Para ver los mensajes del ciclo de vida, abrir Logcat en Android Studio y filtrar por el tag `MainActivity`. Al abrir la app se registra `onStart`, al mandarla a segundo plano `onStop` y al cerrarla `onDestroy`.

Para probar el tema oscuro basta con activar el modo oscuro del sistema en el dispositivo o emulador.
