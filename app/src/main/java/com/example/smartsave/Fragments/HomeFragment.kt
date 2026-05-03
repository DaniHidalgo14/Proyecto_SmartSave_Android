package com.example.smartsave.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.smartsave.R
import com.example.smartsave.controller.Controller
import com.example.smartsave.databinding.FragmentHomeBinding
import com.example.smartsave.model.Usuario
import com.example.smartsave.model.UsuarioViewModel
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private var _binding: FragmentHomeBinding? = null
private val binding get() = _binding!!
private lateinit var usuarioViewModel: UsuarioViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usuarioViewModel = ViewModelProvider(requireActivity())
            .get(UsuarioViewModel::class.java)

        val usuario = usuarioViewModel.usuario
        val controlador = Controller()
        var ingresos_totales : Double? = 0.0
        var gastos_totales : Double?  = 0.0
        var ingreso_mensual : Double?  = 0.0

        lifecycleScope.launch {
            ingresos_totales = controlador.obtenerTotalIng(usuario?.id)
            gastos_totales = controlador.obtenerTotalGas(usuario?.id)
            ingreso_mensual = controlador.obtenerIngresosMensual(usuario?.id)

            binding.totGas.setText("$ingresos_totales €")
            binding.totIng.setText("$gastos_totales €")
            binding.ingresoMensual.text = "$ingreso_mensual €"
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}