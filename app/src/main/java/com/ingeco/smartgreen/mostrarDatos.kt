package com.ingeco.smartgreen

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.DateFormat.*
import java.util.*
import java.util.UUID.randomUUID

import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import android.widget.Toast
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.StringBuilder
import android.system.Os.socket
import android.util.Log








class mostrarDatos : AppCompatActivity() {
    lateinit var btnCerrarSesion: Button
    lateinit var btnNuevoValor: Button
    lateinit var btnVincular: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_datos)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnNuevoValor = findViewById(R.id.btnNuevoValor)
        btnVincular = findViewById(R.id.btnVincular)


        setup()
        visualizarDatos()
        subirDatos()
        recibirDatosBT()
    }


    private fun setup(){

        btnCerrarSesion.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }

    private fun visualizarDatos(){

        /** val Texto = findViewById(R.id.Datos) as TextView
        Texto.setText("Texto de Prueba");
        Texto.setTextColor(Color.RED);
        */

    }


    private fun subirDatos(){

        btnNuevoValor.setOnClickListener {



            val cal = Calendar.getInstance()

            val day: String = Integer.toString(cal.get(Calendar.DAY_OF_MONTH))
            val month: String = Integer.toString(cal.get(Calendar.MONTH)+1)
            val year: String = Integer.toString(cal.get(Calendar.YEAR))

            val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
            val minute: Int = cal.get(Calendar.MINUTE)
            val second: Int = cal.get(Calendar.SECOND)

            var sHour = "0"
            var sMinute = "0"
            var sSecond = "0"


            sHour = if ((hour >= 0) && (hour <=9 )){
                "0" + Integer.toString(hour)
            } else{
                Integer.toString(hour)
            }

            sMinute = if ((minute >= 0) && (minute <=9 )){
                "0" + Integer.toString(minute)
            } else{
                Integer.toString(minute)
            }

            sSecond = if ((second >= 0) && (second <=9 )){
                "0" + Integer.toString(second)
            } else{
                Integer.toString(second)
            }

            val date: String = day + "-" + month + "-" + year

            val time: String = sHour + ":" + sMinute + ":" + sSecond

            val setDate: String = date + "/" + time

            val database = Firebase.database
            val dateValue = database.getReference(setDate)

            data class Data( val data: String? ) {
                // Null default values create a no-argument default constructor, which is needed
                // for deserialization from a DataSnapshot.
            }

            val data: String = "pH: 7 TDS: 1300"

            var Texto : String = "HORA: " + time + ": " + data

            val newData = Data(data)

            dateValue.setValue(newData)


            val arrayAdapter: ArrayAdapter<*>
            var list = arrayOf(
                "Datos tomados en: " + date
            )

            list += Texto


            val listView = findViewById<ListView>(R.id.listDevices)
            arrayAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, list
            )

            listView.adapter = arrayAdapter

        }

    }

    private fun recibirDatosBT(){

        btnVincular.setOnClickListener{

            val intent = Intent(this, BLuetoothActivity::class.java).apply { }

            startActivity(intent)

        }

    }

}
