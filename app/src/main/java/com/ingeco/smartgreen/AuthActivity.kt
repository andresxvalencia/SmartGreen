package com.ingeco.smartgreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    lateinit var btnRegistrar: Button
    lateinit var btnIngresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.SplashTheme)
        Thread.sleep(2000)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnIngresar = findViewById(R.id.btnIngresar)

        setup()
    }

    private fun setup() {

        val emailTxt = findViewById<EditText>(R.id.emailTxt)
        val passwordTxt = findViewById<EditText>(R.id.passwordTxt)

        btnRegistrar.setOnClickListener{
            if(emailTxt.text.isNotEmpty() && passwordTxt.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailTxt.text.toString(),
                passwordTxt.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        showMostrarDatos()
                    } else{
                        showAlert()
                    }
                }
            }
        }

        btnIngresar.setOnClickListener{
            if(emailTxt.text.isNotEmpty() && passwordTxt.text.isNotEmpty()){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailTxt.text.toString(),
                    passwordTxt.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        showMostrarDatos()
                    } else{
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun showMostrarDatos(){
        val intent = Intent(this, mostrarDatos::class.java).apply { }

        startActivity(intent)
        true
    }

}