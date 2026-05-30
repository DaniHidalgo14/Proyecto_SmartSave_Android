package com.example.smartsave.Fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartsave.Adapter.MovAdapter
import com.example.smartsave.R
import com.example.smartsave.controller.Controller
import com.example.smartsave.databinding.FragmentHomeBinding
import com.example.smartsave.databinding.FragmentMovesBinding
import com.example.smartsave.model.Movimiento
import com.example.smartsave.model.Usuario
import com.example.smartsave.model.UsuarioViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.radiobutton.MaterialRadioButton
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var adapter : MovAdapter
private lateinit var usuarioViewModel: UsuarioViewModel

private var _binding: FragmentMovesBinding? = null
private val binding get() = _binding!!

/**
 * A simple [Fragment] subclass.
 * Use the [MovesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controlador = Controller()

        usuarioViewModel = ViewModelProvider(requireActivity())
            .get(UsuarioViewModel::class.java)

        val usuario = usuarioViewModel.usuario

        val recycler = binding.movimientos
        recycler.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            var totalActual =
                BigDecimal(controlador.calcularTotal(usuario?.id)).setScale(2, RoundingMode.HALF_UP)
                    .toDouble()
            binding.tvTotal.text = "${totalActual}€"

            var moves = controlador.obtenerMovimientos(usuario?.id)!!

            adapter = MovAdapter(
                moves,
                onEditClick = { mov : Movimiento ->
                    lifecycleScope.launch {
                        abrirDialogo(controlador, usuario, true, mov)
                    }
                },
                onDeleteClick = { mov : Movimiento ->
                    lifecycleScope.launch {
                        val resultado = controlador.eliminarMovimientp(mov.id_mov)

                        val mensaje = when (resultado) {
                            is Controller.SupabaseResult.Success -> resultado.message
                            is Controller.SupabaseResult.Error -> resultado.error
                        }

                        AlertDialog.Builder(context)
                            .setTitle("Información")
                            .setMessage(mensaje)
                            .setPositiveButton("Aceptar", null)
                            .show()

                        refrescarMovimientos(controlador, usuario)
                    }
                }
            )
            recycler.adapter = adapter
        }

        //========================================================
        //==== Ventana de insercion y edicion de movimientos =====
        //========================================================
        binding.btnIngresos.setOnClickListener {
            abrirDialogo(controlador, usuario, false)
        }
    }

    fun abrirDialogo(controlador : Controller, usuario : Usuario?, editar : Boolean, mov : Movimiento? = null){
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.insert_window, null)
        dialog.setContentView(view)

        // 1. Referencias a los elementos del layout del bottom sheet
        val btnGuardar = view.findViewById<Button>(R.id.btnGuardar)
        val btnIngreso = view.findViewById<Button>(R.id.btnIngreso)
        val btnGasto = view.findViewById<Button>(R.id.btnGasto)
        val fijoBtn = view.findViewById<MaterialRadioButton>(R.id.fijoBtn)
        val spinnerCategoria = view.findViewById<Spinner>(R.id.spinnerCategoria)
        val editConcepto = view.findViewById<EditText>(R.id.etConcepto)
        val editImporte = view.findViewById<EditText>(R.id.etImporte)

        val categorias = listOf(
            R.string.factura,
            R.string.hipoteca,
            R.string.ocio,
            R.string.alimentacion,
            R.string.transporte,
            R.string.otros
        )

        if (editar && mov != null) {

            // Concepto
            editConcepto.setText(mov.subcategoria)

            // Importe
            editImporte.setText(mov.cantidad.toString())

            // Tipo
            if (mov.tipo == "Ingreso") {
                btnIngreso.performClick()
            } else {
                btnGasto.performClick()
            }

            // Fijo
            fijoBtn.isChecked = mov.fijo == 1

            // Categoría (tienes un array de recursos, así que hay que buscar el índice)
            val indexCategoria = categorias.indexOf(mov.categoria)
            if (indexCategoria >= 0) {
                spinnerCategoria.setSelection(indexCategoria)
            }
        }

        //Array de categorias y spinner
        val adapter = object : ArrayAdapter<Int>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categorias
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).text = context.getString(categorias[position])
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                (view as TextView).text = context.getString(categorias[position])
                return view
            }
        }
        spinnerCategoria.adapter = adapter

        //Tipo de movimiento y si es fijo o no
        var tipo : String = ""
        var fijo : Int = 0

        btnIngreso.setOnClickListener {
            tipo = "Ingreso"
        }

        btnGasto.setOnClickListener {
            tipo = "Gasto"
        }

        fijoBtn.setOnClickListener {
            fijo = 1
        }

        // 3. Listener del botón Guardar
        btnGuardar.setOnClickListener {
            var concepto = editConcepto.text.toString()
            var importe = editImporte.text.toString().toDoubleOrNull()
            var categoria = categorias[spinnerCategoria.selectedItemPosition]


            if (concepto.isEmpty() || importe == null || tipo.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch{

                val resultado: Controller.SupabaseResult<Boolean> =
                    if (editar && mov != null) {
                        val movActualizado = mov.copy(
                            subcategoria = concepto,
                            cantidad = importe,
                            categoria = categoria,
                            tipo = tipo,
                            fijo = fijo
                        )

                        controlador.editarMovimiento(movActualizado)
                    } else {
                        controlador.guardarMovimiento(
                            tipo,
                            concepto,
                            importe,
                            categoria,
                            usuario?.id,
                            fijo
                        )
                    }


                val mensaje = when (resultado) {
                    is Controller.SupabaseResult.Success -> resultado.message
                    is Controller.SupabaseResult.Error -> resultado.error
                }

                AlertDialog.Builder(context)
                    .setTitle("Informacion")
                    .setMessage(mensaje)
                    .setPositiveButton("Aceptar", null)
                    .show()

                refrescarMovimientos(controlador, usuario)

            }

            dialog.dismiss()
        }

        dialog.show()
    }

    private suspend fun refrescarMovimientos(controlador: Controller, usuario: Usuario?) {
        val nuevosMovimientos = controlador.obtenerMovimientos(usuario?.id)!!
        adapter.actualizarLista(nuevosMovimientos)
        var totalActual =
            BigDecimal(controlador.calcularTotal(usuario?.id)).setScale(2, RoundingMode.HALF_UP)
                .toDouble()
        binding.tvTotal.text = "${totalActual}€"
    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MovesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}