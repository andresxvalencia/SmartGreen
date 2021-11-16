package com.example.smartgreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"


class MainActivity : AppCompatActivity() {


    lateinit var btnRegistrar: Button
    lateinit var btnIngresar: Button



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        btnRegistrar =  findViewById(R.id.btnRegistrar)


        /*
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
     */
        //setup
        setup()
    }

    private fun setup(){

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        //val message = emailEditText.text.toString()

        btnRegistrar.setOnClickListener{
            if(emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(),
                    passwordEditText.text.toString()).addOnCompleteListener{

                        if(it.isSuccessful) {
                            showMostrarDatos()
                            showAlertA()
                        }
                        else{
                            showAlert()
                        }
                }
            }
        }


/*
        btnIngresar.setOnClickListener{
            if(emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(),
                    passwordEditText.text.toString()).addOnCompleteListener{

                    if(it.isSuccessful) {
                        showMostrarDatos()
                    }
                    else{
                        showAlert()
                    }
                }
            }
        }*/
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertA(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Registro exitoso.")
        builder.setMessage("Su registro ha sido exitoso.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMostrarDatos(){
        val intent = Intent()
        intent.setClassName(this, "com.example.smartgreen.mostrarDatos").apply {
           // putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
        true
    }

}

