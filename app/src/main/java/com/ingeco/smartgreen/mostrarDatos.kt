package com.ingeco.smartgreen

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.DocumentsContract
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.FirebaseError
import com.google.firebase.database.*

import java.util.UUID
import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ChildEventListener

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import java.io.*
import java.lang.Exception
import android.os.Environment





class mostrarDatos : AppCompatActivity() {
    lateinit var btnCerrarSesion: Button
    lateinit var btnNuevoValor: Button
    lateinit var btnDetener: Button
    lateinit var btnSubmitDate: Button
    lateinit var btnDescargar: ImageButton
    lateinit var btnHistorial: ImageButton
    lateinit var btnDeleteData: ImageButton
    var datoNuevo : Boolean = false
    lateinit var datoLeido : String
    var list: MutableList<String> = ArrayList()
    val duration = Toast.LENGTH_SHORT
    var bluetoothIn: Handler? = null
    val handlerState = 0
    private var btAdapter: BluetoothAdapter? = null
    private var btSocket: BluetoothSocket? = null
    private var MyConexionBT: ConnectedThread? = null
    private val BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var begin = false
    private var stop = false
    private var mensaje = ""

    @SuppressLint("HandlerLeak")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_datos)

        bluetoothIn = object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == handlerState) {
                    val MyCaracter = msg.obj as Char
                    if (MyCaracter == 'p') {
                        begin = true
                        stop = false
                        mensaje = ""
                    }
                    if (MyCaracter == '*') {
                        stop = true
                        begin = false
                    }
                    if (begin == true && stop == false) {
                        mensaje = mensaje + MyCaracter
                    }
                    if (begin == false && stop == true) {
                        recbirMensaje(mensaje)
                        begin = false
                        stop = false
                    }
                }
            }
        }
        btAdapter = BluetoothAdapter.getDefaultAdapter()

        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnNuevoValor = findViewById(R.id.btnNuevoValor)
        btnDetener = findViewById(R.id.btnDetener)
        btnHistorial = findViewById(R.id.btnHistorial)
        btnSubmitDate = findViewById(R.id.btnSubmitDate)
        btnDeleteData= findViewById(R.id.btnDeleteData)
        // btnDescargar = findViewById(R.id.btnDescargar)
        setup()
        subirDatos()
        descargarDatos()
        historial()
        deleteData()
    }

    private fun setup() {

        btnCerrarSesion.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }

    private fun subirDatos() {

        btnNuevoValor.setOnClickListener {

            createConnection()

            definirFecha()

        }

    }

    private fun definirFecha() {

        val cal = Calendar.getInstance()

        val day: String = Integer.toString(cal.get(Calendar.DAY_OF_MONTH))
        val month: String = Integer.toString(cal.get(Calendar.MONTH) + 1)
        val year: String = Integer.toString(cal.get(Calendar.YEAR))

        val date: String = day + "-" + month + "-" + year

        val fecha = findViewById<TextView>(R.id.Fecha)

        fecha.setText(" Fecha cuando se tomaron los datos: "+ date)

    }

    private fun nuevaMuestra(lectura: String) {

        val cal = Calendar.getInstance()

        val day: String = Integer.toString(cal.get(Calendar.DAY_OF_MONTH))
        val month: String = Integer.toString(cal.get(Calendar.MONTH) + 1)
        val year: String = Integer.toString(cal.get(Calendar.YEAR))

        val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
        val minute: Int = cal.get(Calendar.MINUTE)
        val second: Int = cal.get(Calendar.SECOND)

        var sHour: String
        var sMinute: String
        var sSecond: String


        sHour = if ((hour >= 0) && (hour <= 9)) {
            "0" + Integer.toString(hour)
        } else {
            Integer.toString(hour)
        }

        sMinute = if ((minute >= 0) && (minute <= 9)) {
            "0" + Integer.toString(minute)
        } else {
            Integer.toString(minute)
        }

        sSecond = if ((second >= 0) && (second <= 9)) {
            "0" + Integer.toString(second)
        } else {
            Integer.toString(second)
        }

        val date: String = day + "-" + month + "-" + year

        val time: String = sHour + ":" + sMinute + ":" + sSecond


        val database = Firebase.database
        val dateValue = database.getReference(date)

        data class Data(val data: MutableList <String> = ArrayList()) {

        }

        val data: String = lectura

        var Texto = "HORA: " + time + ": " + data

        val newData = Data(list)

        dateValue.setValue(newData)

        val arrayAdapter: ArrayAdapter<*>

        var lista = list

        lista.add(Texto)


        val listView = findViewById<ListView>(R.id.listDevices)
        arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, lista
        )

        listView.adapter = arrayAdapter
    }

    private fun recbirMensaje(lectura: String) {

        datoLeido = lectura
        datoNuevo = true

        nuevaMuestra(lectura)

    }

    private fun descargarDatos() {
        
    }

    private fun historial() {
        btnHistorial.setOnClickListener {

            val dateSelected : EditText = findViewById(R.id.editTextDate)

            dateSelected.setVisibility(View.VISIBLE)
            btnSubmitDate.setVisibility(View.VISIBLE)

            btnSubmitDate.setOnClickListener {


                val date = dateSelected.getText().toString()

                val fecha = findViewById<TextView>(R.id.Fecha)

                fecha.setText(" Fecha cuando se tomaron los datos: "+ date)

                class Post() {
                    var data: MutableList<String> = ArrayList()

                }

                val database = FirebaseDatabase.getInstance()
                val ref = database.getReference(date)

                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val post = dataSnapshot.getValue<Post>()
                        val receivedData = post?.data as MutableList<String>
                        sendData(receivedData)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        println("The read failed: " + databaseError.code)
                    }
                })
            }

        }

    }

    private fun sendData(historyList: MutableList <String>) {

        val arrayAdapter: ArrayAdapter<*>

        var lista = historyList



        val listView = findViewById<ListView>(R.id.listDevices)
        arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, lista
        )

        listView.adapter = arrayAdapter
    }

    private fun deleteData(){
        btnDeleteData.setOnClickListener {

            val fecha = findViewById<TextView>(R.id.Fecha)

            val dateLong = fecha.getText().toString()

            val dateString = dateLong.substring(dateLong.length-10)

            val database = FirebaseDatabase.getInstance()

            val date = database.getReference(dateString+"/data")

            date.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.ref.removeValue()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Delete", "onCancelled", databaseError.toException())
                }
            })

            list = arrayListOf()
            val arrayAdapter: ArrayAdapter<*>

            val listView = findViewById<ListView>(R.id.listDevices)
            arrayAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1, list
            )

            listView.adapter = arrayAdapter

            val text = "Se ha borrado la muestra."

            val toast = Toast.makeText(applicationContext,text,duration)
            toast.show()
        }
    }

    @Throws(IOException::class)
    private fun createBluetoothSocket(device: BluetoothDevice): BluetoothSocket {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID)
    }

    fun createConnection() {
        //Setea la direccion MAC
        val device = btAdapter!!.getRemoteDevice("98:D3:71:FD:48:F5")
        try {
            btSocket = createBluetoothSocket(device)
        } catch (e: IOException) {
            Toast.makeText(baseContext, "La creacción del Socket fallo", Toast.LENGTH_LONG).show()
        }
        // Establece la conexión con el socket Bluetooth.
        try {
            btSocket!!.connect()
            val text = "Conectado"
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        } catch (e: IOException) {
            try {
                btSocket!!.close()
            } catch (e2: IOException) {
            }
        }

        MyConexionBT = bluetoothIn?.let { ConnectedThread(btSocket!!, it) }
        MyConexionBT!!.start()

        fun cancel() {
            try {
               btSocket?.close()
                val text = "Desconectado"
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()

            } catch (e: IOException) {
                Log.e("BT", "Could not close the connect socket", e)
            }
        }

        btnDetener.setOnClickListener {

            cancel()

        }

    }

    private class ConnectedThread(socket: BluetoothSocket, handler: Handler?) : Thread() {
        private var bluetoothIn: Handler? = handler
        private val mmInStream: InputStream?
        private val mmOutStream: OutputStream?
        override fun run() {
            val byte_in = ByteArray(1)
            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    mmInStream!!.read(byte_in)
                    val ch = byte_in[0].toInt().toChar()
                    bluetoothIn?.obtainMessage(0, ch)?.sendToTarget()

                } catch (e: IOException) {
                    break
                }
            }
        }

        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null
            try {
                tmpIn = socket.inputStream
                tmpOut = socket.outputStream
            } catch (e: IOException) {
            }
            mmInStream = tmpIn
            mmOutStream = tmpOut
        }
    }
}
