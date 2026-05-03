package com.example.smartsave.controller

import com.example.smartsave.model.IngresoMensual
import com.example.smartsave.model.Movimiento
import com.example.smartsave.model.Usuario
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest

class Controller {
    private val url : String = "https://vsjqalaneacuduhnwovn.supabase.co"
    private val apiKey : String = "sb_publishable_a85lyTswAKlzKhsOeRQ16A_PWQyFooF"
    private val client = createSupabaseClient(supabaseUrl = url, supabaseKey = apiKey) {
        install(Postgrest)
    }

    suspend fun obtenerAcceso(usuario : String, password : String) : Usuario? {
        return try{
            var result = client.postgrest["Usuario"].select {
                filter {
                    eq("Usuario", usuario)
                    eq("Password", password)
                }
            }
            val user = result.decodeList<Usuario>()
            user.firstOrNull()
        }catch (e : Exception){
            e.printStackTrace()
            null
        }
    }

    suspend fun obtenerMovimientos(userId : Int?) : List<Movimiento>? {
        return try {
            var movimientos = client.postgrest["Movimientos"].select {
                filter { eq("id_usuario", userId!!) }
            }.decodeList<Movimiento>()
            println("Movimiento: $movimientos")
            movimientos
        }catch (e : Exception){
            e.printStackTrace()
            null
        }
    }

    suspend fun obtenerTotalIng(userId : Int?) : Double? {
        return try{
            var ingresos = client.postgrest["Movimientos"].select {
                filter {
                    eq("Tipo", "Ingreso")
                    eq("id_usuario", userId!!)
                }
            }.decodeList<Movimiento>()
            var total : Double = 0.0
            for (mov in ingresos){
                total += mov.cantidad
            }
            total
        }catch(e : Exception){
            e.printStackTrace()
            null
        }
    }

    suspend fun obtenerTotalGas(userId : Int?) : Double? {
        return try{
            var ingresos = client.postgrest["Movimientos"].select {
                filter {
                    eq("Tipo", "Gasto")
                    eq("id_usuario", userId!!)
                }
            }.decodeList<Movimiento>()
            var total : Double = 0.0
            for (mov in ingresos){
                total += mov.cantidad
            }
            total
        }catch(e : Exception){
            e.printStackTrace()
            null
        }
    }

    suspend fun obtenerIngresosMensual(userId: Int?) : Double? {
        return try{
            var ingresoM = client.postgrest["Ingreso_mensual"].select {
                filter {
                    eq("id_usuario", userId!!)
                }
            }.decodeList<IngresoMensual>()
            ingresoM.firstOrNull()?.ingreso
        }catch (e : Exception){
            e.printStackTrace()
            null
        }
    }
}