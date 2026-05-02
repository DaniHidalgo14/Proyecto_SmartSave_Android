package com.example.smartsave.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartsave.R
import com.example.smartsave.model.Movimiento

class MovAdapter(private val lista : List<Movimiento>) : RecyclerView.Adapter<MovAdapter.ViewHolder>() {

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val imagen = view.findViewById<ImageView>(R.id.categoriaIV)
        val categoria = view.findViewById<TextView>(R.id.categoriaTv)
        val cantidad = view.findViewById<TextView>(R.id.cantidad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movimientos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movimiento = lista[position]
        holder.categoria.text = movimiento.categoria
        holder.cantidad.text = "${movimiento.cantidad} €"
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}