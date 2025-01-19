package com.example.wemeet_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService4 {
    @GET("application/getUsuario")
    suspend fun getUsuario(@Query("id") userId: Int): Perfil
}

class MainActivity7 : AppCompatActivity() {

    private lateinit var listaEspaciosButton: Button
    private lateinit var tusEspaciosButton: Button
    private lateinit var espaciosAlquilados: Button
    private lateinit var closeSessonButon : Button

    private lateinit var name: TextView
    private lateinit var timesRent: TextView
    private lateinit var timesTenant: TextView
    private lateinit var apiService: ApiService4



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main7)

        name = findViewById(R.id.name)
        timesRent = findViewById(R.id.timesRent)
        timesTenant = findViewById(R.id.timesTenant)
        listaEspaciosButton = findViewById(R.id.button)
        tusEspaciosButton = findViewById(R.id.button2)
        espaciosAlquilados = findViewById(R.id.button3)
        closeSessonButon = findViewById(R.id.button4)

        listaEspaciosButton.setOnClickListener {
            val intent = Intent(this@MainActivity7, MainActivity3::class.java)
            startActivity(intent)
        }
        tusEspaciosButton.setOnClickListener {
            val intent = Intent(this@MainActivity7, MainActivity5::class.java)
            startActivity(intent)
        }
        espaciosAlquilados.setOnClickListener {
            val intent = Intent(this@MainActivity7, MainActivity6::class.java)
            startActivity(intent)
        }
        closeSessonButon.setOnClickListener{
            val intent = Intent(this@MainActivity7, MainActivity2::class.java)
            id0 = -2
            startActivity(intent)
        }


        obtenerDatosPerfil(id0)
    }

    private fun mostrarDatosPerfil(perfil: Perfil) {
        name.text = perfil.Name
        timesRent.text = perfil.numberTimesRent
        timesTenant.text = perfil.numberTimesTenant
    }

    private suspend fun realizarPeticionGet(userId: Int): Perfil {
        val retrofit = Retrofit.Builder()
            .baseUrl(url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        Log.i("MainActivity7", "Numero 3 ")
        apiService = retrofit.create(ApiService4::class.java)
        Log.i("MainActivity7", "Numero 4")
        //Log.i("MainActivity7", "Nom: ${apiService.getUsuario(userId)}")
        return apiService.getUsuario(userId)
    }

    private fun obtenerDatosPerfil(userId: Int) {
        lifecycleScope.launch {
            try {
                Log.i("MainActivity7", "Numero 1")
                val perfil = realizarPeticionGet(userId)
                Log.i("MainActivity7", "Numero 2")
                mostrarDatosPerfil(perfil)
            } catch (e: Exception) {
                // errores
            }
        }
    }





}