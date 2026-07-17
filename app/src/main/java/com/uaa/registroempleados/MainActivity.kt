package com.uaa.registroempleados

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uaa.registroempleados.model.Empleado
import com.uaa.registroempleados.ui.theme.RegistroEmpleadosTheme

/**
 * Activity principal de la aplicación.
 * Sobrescribe onStart, onStop y onDestroy registrando un mensaje de log
 * en cada método, según lo solicitado en los requisitos.
 */
class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroEmpleadosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegistroEmpleadosApp()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}

/**
 * Pantalla principal: formulario de ingreso arriba y lista de empleados debajo.
 * Se usa una única LazyColumn para que toda la pantalla sea desplazable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroEmpleadosApp() {
    // Lista de empleados en memoria, observable por Compose
    val empleados = remember { mutableListOf<Empleado>().toMutableStateList() }
    var proximoId by rememberSaveable { mutableStateOf(1L) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Registro de Empleados",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Formulario de ingreso de datos
            item {
                FormularioEmpleado(
                    onRegistrar = { nombre, cargo, departamento, salario, fecha ->
                        empleados.add(
                            Empleado(
                                id = proximoId,
                                nombreCompleto = nombre,
                                cargo = cargo,
                                departamento = departamento,
                                salario = salario,
                                fechaContratacion = fecha
                            )
                        )
                        proximoId++
                    }
                )
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = if (empleados.isEmpty())
                        "Aún no hay empleados registrados"
                    else
                        "Empleados registrados (${empleados.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Lista vertical de empleados (LazyColumn)
            items(items = empleados, key = { it.id }) { empleado ->
                EmpleadoItem(
                    empleado = empleado,
                    onEliminar = { empleados.remove(empleado) }
                )
            }
        }
    }
}

/**
 * Formulario para registrar un nuevo empleado.
 */
@Composable
fun FormularioEmpleado(
    onRegistrar: (String, String, String, String, String) -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var cargo by rememberSaveable { mutableStateOf("") }
    var departamento by rememberSaveable { mutableStateOf("") }
    var salario by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var mostrarError by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Nuevo empleado",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                singleLine = true,
                isError = mostrarError && nombre.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = cargo,
                onValueChange = { cargo = it },
                label = { Text("Cargo") },
                singleLine = true,
                isError = mostrarError && cargo.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = departamento,
                onValueChange = { departamento = it },
                label = { Text("Departamento") },
                singleLine = true,
                isError = mostrarError && departamento.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = salario,
                onValueChange = { salario = it },
                label = { Text("Salario (Gs.)") },
                singleLine = true,
                isError = mostrarError && salario.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha de contratación (dd/mm/aaaa)") },
                singleLine = true,
                isError = mostrarError && fecha.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )

            if (mostrarError) {
                Text(
                    text = "Completá todos los campos para registrar al empleado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    val camposCompletos = listOf(nombre, cargo, departamento, salario, fecha)
                        .all { it.isNotBlank() }
                    if (camposCompletos) {
                        onRegistrar(
                            nombre.trim(),
                            cargo.trim(),
                            departamento.trim(),
                            salario.trim(),
                            fecha.trim()
                        )
                        nombre = ""
                        cargo = ""
                        departamento = ""
                        salario = ""
                        fecha = ""
                        mostrarError = false
                    } else {
                        mostrarError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registrar empleado")
            }
        }
    }
}

/**
 * Ítem de la lista de empleados.
 * Estructura solicitada:
 *  - Nombre completo destacado en la parte superior
 *  - LazyRow con los demás datos (cargo, departamento, salario, fecha)
 *  - Botón de eliminación en la parte inferior
 */
@Composable
fun EmpleadoItem(
    empleado: Empleado,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Nombre destacado
            Text(
                text = empleado.nombreCompleto,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // LazyRow con los demás datos del empleado
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    DatoEmpleadoChip(
                        icono = Icons.Default.Work,
                        etiqueta = "Cargo",
                        valor = empleado.cargo
                    )
                }
                item {
                    DatoEmpleadoChip(
                        icono = Icons.Default.Apartment,
                        etiqueta = "Departamento",
                        valor = empleado.departamento
                    )
                }
                item {
                    DatoEmpleadoChip(
                        icono = Icons.Default.Payments,
                        etiqueta = "Salario",
                        valor = empleado.salario
                    )
                }
                item {
                    DatoEmpleadoChip(
                        icono = Icons.Default.CalendarMonth,
                        etiqueta = "Contratación",
                        valor = empleado.fechaContratacion
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón de eliminación en la parte inferior
            OutlinedButton(
                onClick = onEliminar,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar a ${empleado.nombreCompleto}"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Eliminar")
            }
        }
    }
}

/**
 * Tarjeta pequeña que muestra un dato individual del empleado dentro del LazyRow.
 */
@Composable
fun DatoEmpleadoChip(
    icono: ImageVector,
    etiqueta: String,
    valor: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = valor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
