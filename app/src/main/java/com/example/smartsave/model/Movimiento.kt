package com.example.smartsave.model

import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as JavaSerializable

@JavaSerializable
data class Movimiento(
    @SerialName("id_mov") val id_mov : Int,
    @SerialName("Tipo") val tipo : String,
    @SerialName("Cantidad") val cantidad : Double,
    @SerialName("Fijo") val fijo : Int,
    @SerialName("id_usuario") val id_usuario : Int,
    @SerialName("Categoria") val categoria : Int,
    @SerialName("Subcategoria") val subcategoria : String?
) : Serializable

@JavaSerializable
data class MovimientoInsert(
    @SerialName("Tipo") val tipo : String,
    @SerialName("Cantidad") val cantidad : Double,
    @SerialName("Fijo") val fijo : Int,
    @SerialName("id_usuario") val id_usuario : Int,
    @SerialName("Categoria") val categoria : Int,
    @SerialName("Subcategoria") val subcategoria : String?
) : Serializable
