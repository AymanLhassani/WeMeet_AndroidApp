package com.example.wemeet_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("application/listSpacesAndroid")
    suspend fun getEspacios(): List<Espacio>
}

class MainActivity3 : AppCompatActivity() {

    private lateinit var homeButton: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: EspacioAdapter
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        recyclerView = findViewById(R.id.espaciosRecyclerView)
        progressBar = findViewById(R.id.loadingProgressBar)
        homeButton = findViewById(R.id.homeButton)

        setupRecyclerView()
        setupRetrofit()
        fetchEspacios()

        homeButton.setOnClickListener{
            val intent = Intent(this@MainActivity3, MainActivity7::class.java)
            startActivity(intent)
        }

    }

    private fun setupRecyclerView() {
        adapter = EspacioAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private fun fetchEspacios() {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                val espacios = apiService.getEspacios()
                adapter.updateEspacios(espacios)
                progressBar.visibility = View.GONE
            } catch (e: Exception) {
                // Manejar el error
                progressBar.visibility = View.GONE
            }
        }
    }
}