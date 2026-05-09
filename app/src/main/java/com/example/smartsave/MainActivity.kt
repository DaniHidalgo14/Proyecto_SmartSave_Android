package com.example.smartsave

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.smartsave.Fragments.ConfigFragment
import com.example.smartsave.Fragments.HomeFragment
import com.example.smartsave.Fragments.MovesFragment
import com.example.smartsave.model.Usuario
import com.example.smartsave.model.UsuarioViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var usuarioViewModel: UsuarioViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }
        //TODO: ARREGLAR UN POCO EL LAYOUT Y COMENZAR CON LAS VENTANAS DE INSERTS Y EDICIONES
        val usuario = intent.getSerializableExtra("Usuario") as Usuario

        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)
        usuarioViewModel.usuario = usuario

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
    }
}