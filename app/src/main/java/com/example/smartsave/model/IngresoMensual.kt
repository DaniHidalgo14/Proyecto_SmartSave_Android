package com.example.smartsave.model

import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as JavaSerializable

@JavaSerializable
data class IngresoMensual(
    @SerialName("id_usuario") val id_usuario : Int,
    @SerialName("Ingreso") val ingreso : Double
) : Serializable
