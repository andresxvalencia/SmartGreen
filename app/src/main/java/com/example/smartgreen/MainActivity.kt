package com.example.smartgreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"


class MainActivity : AppCompatActivity() {


    lateinit var btnIngresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIngresar =  findViewById(R.id.btnIngresar)
        val editText = findViewById<EditText>(R.id.idUsuario)
        val message = editText.text.toString()

        btnIngresar.setOnClickListener{
            val intent = Intent()
            intent.setClassName(this, "com.example.smartgreen.mostrarDatos").apply {
                putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
            true
        }


    }





}