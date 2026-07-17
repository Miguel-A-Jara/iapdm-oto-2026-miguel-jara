package com.uaa.registroempleados.model

/**
 * Modelo de datos que representa a un empleado registrado en la aplicación.
 */
data class Empleado(
    val id: Long,
    val nombreCompleto: String,
    val cargo: String,
    val departamento: String,
    val salario: String,
    val fechaContratacion: String
)
