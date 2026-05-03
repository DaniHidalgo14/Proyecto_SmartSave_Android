package com.example.smartsave.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartsave.R
import com.example.smartsave.model.Movimiento

class MovAdapter(private val lista : List<Movimiento>) : RecyclerView.Adapter<MovAdapter.ViewHolder>() {
    private val total = lista.sumOf { it.cantidad }
    //TODO: CREAR LAS LISTAS RESPECTIVAS DE INGRESOS Y GASTOS Y CALCULAR PORCENTAJES SOBRE ELLAS (VERDE INGRESOS, ROJO GASTOS)

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val imagen = view.findViewById<ImageView>(R.id.categoriaIV)
        val categoria = view.findViewById<TextView>(R.id.categoriaTv)
        val cantidad = view.findViewById<TextView>(R.id.cantidad)

        val barra = view.findViewById<View>(R.id.barraPorcentaje)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movimientos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movimiento = lista[position]

        var porcentaje = (movimiento.cantidad / total.toFloat()) * 100

        holder.categoria.text = movimiento.categoria
        holder.cantidad.text = "${movimiento.cantidad} €"

        val params = holder.barra.layoutParams
        params.width = (porcentaje * 3).toInt()  // multiplicas para que sea visible
        holder.barra.layoutParams = params

        // Color aleatorio
        holder.barra.setBackgroundColor(Color.RED)
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}