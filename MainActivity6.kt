package com.example.wemeet_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService3 {
    @GET("application/listaEspaciosAlquiladosAndroid")
    suspend fun getEspacios(@Query("id") id: Int) : List<Espacio>
}

class MainActivity6 : AppCompatActivity() {

    private lateinit var homeButton: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: EspacioAdapterAlquilado
    private lateinit var apiService: ApiService3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)

        recyclerView = findViewById(R.id.espaciosRecyclerView)
        progressBar = findViewById(R.id.loadingProgressBar)
        homeButton = findViewById(R.id.homeButton)

        setupRecyclerView()
        setupRetrofit()
        fetchEspacios()

        homeButton.setOnClickListener{
            val intent = Intent(this@MainActivity6, MainActivity7::class.java)
            startActivity(intent)
        }

    }

    private fun setupRecyclerView() {
        adapter = EspacioAdapterAlquilado(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService3::class.java)
    }

    private fun fetchEspacios() {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                val espacios = apiService.getEspacios(id0)
                adapter.updateEspacios(espacios)
                progressBar.visibility = View.GONE
            } catch (e: Exception) {
                // Manejar el error
                progressBar.visibility = View.GONE
            }
        }
    }
}