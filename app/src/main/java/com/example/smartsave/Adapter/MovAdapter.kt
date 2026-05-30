package com.example.smartsave.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartsave.R
import com.example.smartsave.controller.Controller
import com.example.smartsave.model.Movimiento

class MovAdapter(
    private var lista: List<Movimiento>,
    private val onEditClick: (Movimiento) -> Unit,
    private val onDeleteClick: (Movimiento) -> Unit
) : RecyclerView.Adapter<MovAdapter.ViewHolder>() {

    private val listaIngresos = lista.filter { it.tipo.equals("Ingreso") }
    private val listaGastos = lista.filter { it.tipo.equals("Gasto") }

    var sumIng = listaIngresos.sumOf { it.cantidad }
    var sumGas = listaGastos.sumOf { it.cantidad }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val card = view.findViewById<CardView>(R.id.card)
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
        val controller = Controller()

        when (movimiento.categoria) {
            R.string.alimentacion -> holder.imagen.setImageResource(R.drawable.foodbuys_iv)
            R.string.transporte -> holder.imagen.setImageResource(R.drawable.transport_iv)
            R.string.hipoteca -> holder.imagen.setImageResource(R.drawable.home_iv)
            R.string.factura -> holder.imagen.setImageResource(R.drawable.invoice_iv)
            R.string.ocio -> holder.imagen.setImageResource(R.drawable.ocio_iv)
            R.string.otros -> holder.imagen.setImageResource(R.drawable.otros_iv)
        }



        holder.categoria.text = holder.itemView.context.getString(movimiento.categoria)
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

        holder.card.setOnLongClickListener {
            val popup = PopupMenu(holder.itemView.context, it)
            popup.menuInflater.inflate(R.menu.menu_movimiento, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.btnEditar -> {
                        onEditClick(movimiento)
                        true
                    }
                    R.id.btnEliminar -> {
                        onDeleteClick(movimiento)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
            true
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    fun actualizarLista(nuevaLista: List<Movimiento>) {
        lista = nuevaLista
        sumIng = lista.filter { it.tipo == "Ingreso" }.sumOf { it.cantidad }
        sumGas = lista.filter { it.tipo == "Gasto" }.sumOf { it.cantidad }
        notifyDataSetChanged()
    }

}