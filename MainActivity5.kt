package com.example.wemeet_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService2 {
    @GET("application/listaEspaciosPropiosAndroid")
    suspend fun getEspacios(@Query("id") id: Int) : List<Espacio>
}

class MainActivity5 : AppCompatActivity() {

    private lateinit var addSpaceButton: Button
    private lateinit var homeButton: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: EspacioAdapterPropio
    private lateinit var apiService: ApiService2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        addSpaceButton = findViewById(R.id.addSpaceButton)
        recyclerView = findViewById(R.id.espaciosRecyclerView)
        progressBar = findViewById(R.id.loadingProgressBar)
        homeButton = findViewById(R.id.homeButton)

        setupRecyclerView()
        setupRetrofit()
        fetchEspacios()

        addSpaceButton.setOnClickListener {
            val intent = Intent(this@MainActivity5, MainActivity4::class.java)
            startActivity(intent)
        }
        homeButton.setOnClickListener{
            val intent = Intent(this@MainActivity5, MainActivity7::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = EspacioAdapterPropio(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService2::class.java)
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