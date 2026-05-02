package com.example.smartsave.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serializable as JavaSerializable

@Serializable
data class Usuario(
    @SerialName("id") val id: Int? = null,
    @SerialName("Creado") val creado: String? = null,
    @SerialName("Usuario") val usuario: String? = null,
    @SerialName("Password") val password: String? = null,
    @SerialName("Email") val email: String? = null,
    @SerialName("Ultimo_acceso") val ultimo_acceso : String? = null,
    @SerialName("Total") val total: Double? = null,
    @SerialName("Actualizar_el") val actualizar_el: String? = null
) : JavaSerializable
