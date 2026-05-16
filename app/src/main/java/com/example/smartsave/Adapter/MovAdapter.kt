package com.example.smartsave.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartsave.R
import com.example.smartsave.model.Movimiento

class MovAdapter(private val lista : List<Movimiento>) : RecyclerView.Adapter<MovAdapter.ViewHolder>() {
    //TODO: ARREGLAR PROBLEMA CON IVS DE LOS ITEMS DEL RECYCLER
    private val listaIngresos = lista.filter { it.tipo.equals("Ingreso") }
    private val listaGastos = lista.filter { it.tipo.equals("Gasto") }

    val sumIng = listaIngresos.sumOf { it.cantidad }
    val sumGas = listaGastos.sumOf { it.cantidad }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val imagen = view.findViewById<ImageView>(R.id.categoriaIV)
        val categoria = view.findViewById<TextView>(R.id.categoriaTv)
        val subcategoria = view.findViewById<TextView>(R.id.subcategoriaTv)
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
        Log.d("DEBUG", "categoria: '${movimiento.categoria}' | tipo: '${movimiento.tipo}'")

        when(movimiento.categoria.trim()){
            "ALIMENTACION" -> holder.imagen.setImageResource(R.drawable.foodbuys_iv)
            "TRANSPORTE" -> holder.imagen.setImageResource(R.drawable.transport_iv)
            "HIPOTECA" -> holder.imagen.setImageResource(R.drawable.home_iv)
            "FACTURAS" -> holder.imagen.setImageResource(R.drawable.invoice_iv)
            "OCIO" -> holder.imagen.setImageResource(R.drawable.ocio_iv)
            "OTROS" -> holder.imagen.setImageResource(R.drawable.otros_iv)
        }

        holder.categoria.text = movimiento.categoria
        if(movimiento.subcategoria != null) holder.subcategoria.text = movimiento.subcategoria

        if(movimiento.tipo.equals("Ingreso")){
            var porcentajeIng = (movimiento.cantidad / sumIng.toFloat()) * 100

            val params = holder.barra.layoutParams
            params.width = (porcentajeIng * 3).toInt()
            holder.barra.layoutParams = params

            holder.cantidad.setTextColor(Color.GREEN)
            holder.cantidad.text = "+${movimiento.cantidad} €"
            holder.barra.setBackgroundColor(Color.GREEN)
        }else{
            var porcentajeGas = (movimiento.cantidad / sumGas.toFloat()) * 100

            val params = holder.barra.layoutParams
            params.width = (porcentajeGas * 3).toInt()
            holder.barra.layoutParams = params

            holder.cantidad.setTextColor(Color.RED)
            holder.cantidad.text = "-${movimiento.cantidad} €"
            holder.barra.setBackgroundColor(Color.RED)
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}