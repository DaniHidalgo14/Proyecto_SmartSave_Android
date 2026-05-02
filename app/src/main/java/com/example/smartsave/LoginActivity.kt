package com.example.smartsave

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.smartsave.controller.Controller
import com.example.smartsave.model.Usuario
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var miUsuario : Usuario? = null
        val controlador = Controller()
        var btnAcceder = findViewById<Button>(R.id.button)
        var usuarioTxt = findViewById<TextInputEditText>(R.id.usuario)
        var passWordTxt = findViewById<TextInputEditText>(R.id.password)

        //TODO: PROBAR QUE LA SESION FUNCIONA AL VOLVER AL LOGIN SE DESACTIVA EL USUARIO
        usuarioTxt.setText("")
        passWordTxt.setText("")

        btnAcceder.setOnClickListener {

            lifecycleScope.launch {
                var usuario = usuarioTxt.text.toString().trim()
                var password = passWordTxt.text.toString().trim()

                miUsuario = controlador.obtenerAcceso(usuario, password)

                if(miUsuario?.id == null){
                    Toast.makeText(this@LoginActivity, "Error: usuario/contraseña incorrectos", Toast.LENGTH_LONG).show()
                }else{
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("Usuario", miUsuario)
                    startActivity(intent)
                }
            }
        }
    }
}