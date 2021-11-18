package com.ingeco.smartgreen

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.*


class mostrarDatos : AppCompatActivity() {
    lateinit var btnCerrarSesion: Button
    lateinit var btnNuevoValor: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_datos)

        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnNuevoValor = findViewById(R.id.btnNuevoValor)

        setup()
        visualizarDatos()
        subirDatos()
    }


    private fun setup(){

        btnCerrarSesion.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }

    private fun visualizarDatos(){

        val Texto = findViewById(R.id.Datos) as TextView
        Texto.setText("Texto de Prueba");
        Texto.setTextColor(Color.RED);

    }


    private fun subirDatos(){

        btnNuevoValor.setOnClickListener{

            val hora: String = DateFormat.getDateTimeInstance().format(Date())
            val database = Firebase.database
            val phValue = database.getReference("pH")
            val TDSValue = database.getReference("TDS")
            val timeValue = database.getReference("Time")
            phValue.setValue(7.5)
            TDSValue.setValue(1200)
            timeValue.setValue(hora)

        }

    }

}