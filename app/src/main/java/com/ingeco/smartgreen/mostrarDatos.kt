package com.ingeco.smartgreen

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.util.Strings
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
import java.lang.reflect.Array.get









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

            data class Data( val TDS: String? = null, val pH: String?) {
                // Null default values create a no-argument default constructor, which is needed
                // for deserialization from a DataSnapshot.
            }

            val TDS: String = "1300"
            val pH: String = "7"

            var Texto : String = "Hora: " + time + "        pH: " + pH + "      TDS: " + TDS

            val newData = Data(TDS,pH)

            dateValue.setValue(newData)

            val TextDate = findViewById(R.id.Fecha) as TextView
            TextDate.setText("Fecha donde se tomaron los datos: " + date);
            TextDate.setTextColor(Color.BLUE);

            val TextData = findViewById(R.id.Datos) as TextView
            TextData.setText(Texto);
            TextData.setTextColor(Color.BLUE);
            TextData.setTextSize(20F);


            Texto = Texto + "\n"

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
