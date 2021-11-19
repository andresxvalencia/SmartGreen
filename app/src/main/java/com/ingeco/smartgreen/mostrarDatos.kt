package com.ingeco.smartgreen

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.UUID.randomUUID
import org.json.JSONObject
import org.json.JSONException






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

        val Texto = findViewById(R.id.Datos) as TextView
        Texto.setText("Texto de Prueba");
        Texto.setTextColor(Color.RED);

    }


    private fun subirDatos(){

        btnNuevoValor.setOnClickListener {


            val database = Firebase.database
            val hora: String = getDateInstance().format(Date())
            val dateValue = database.getReference("Nov 19 2021")


            data class Data(val Time: String? = null, val TDS: String? = null, val pH: String?) {
                // Null default values create a no-argument default constructor, which is needed
                // for deserialization from a DataSnapshot.
            }

            val newData = Data(hora,"1300","7")

            dateValue.setValue(newData)

        }

    }

    private fun recibirDatosBT(){

        btnVincular.setOnClickListener{
            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter == null) {
                // Device doesn't support Bluetooth
            }
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address

                val name = findViewById(R.id.deviceName) as TextView
                name.setText(deviceName);
                name.setTextColor(Color.RED);

                val address = findViewById(R.id.deviceAddress) as TextView
                address.setText(deviceHardwareAddress);
                address.setTextColor(Color.RED);

                val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
                    bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord("BT", randomUUID ())
                }

            }
        }
    }
}
