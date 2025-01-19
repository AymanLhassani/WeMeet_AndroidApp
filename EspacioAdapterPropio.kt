package com.example.wemeet_app

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import com.example.wemeet_app.EspacioAdapterPropio.EspacioViewPropio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import retrofit2.http.Query


class EspacioAdapterPropio(private var espacios: List<Espacio>) :
    RecyclerView.Adapter<EspacioViewPropio>() {

    class EspacioViewPropio(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numberPeople: TextView = itemView.findViewById(R.id.numeroEspacioTextView)
        val schedule: TextView = itemView.findViewById(R.id.scheduleEspacioTextView)
        val location: TextView = itemView.findViewById(R.id.locationEspacioTextView)
        val ventilation: TextView = itemView.findViewById(R.id.ventilationEspacioTextView)
        val deleteSpaceButton : Button = itemView.findViewById(R.id.deleteSpaceButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspacioViewPropio {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_espacio_propios, parent, false)
        return EspacioViewPropio(view)
    }

    override fun onBindViewHolder(x : EspacioViewPropio, position: Int) {
        val espacio = espacios[position]
        x.numberPeople.text = "Personas que admite: " + espacio.numberPeople.toString() + " Personas"
        x.schedule.text = "Horario disponible: " + espacio.scheduleAvailable
        x.location.text = espacio.location
        if(espacio.ventilation) {
            x.ventilation.text = "Si tiene ventilacion"
        }else{
            x.ventilation.text = "No tiene ventilacion"
        }
        x.deleteSpaceButton.setOnClickListener{
            fetchData(id0.toLong(), espacio.numberPeople.toInt(), espacio.scheduleAvailable, espacio.location, espacio.ventilation.toString())
        }
    }

    interface ApiService {
        @GET("application/deleteSpace")
        fun getData(@Query("id") id: Long, @Query("numberPeople") numberPeople : Int, @Query("scheduleAvailable") scheduleAvailable : String, @Query("location") location : String, @Query("ventilation") ventilation : String) : Call<String>
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun fetchData(id0 : Long, numberPeople: Int, scheduleAvailable: String, location: String,ventilation: String) {
        val apiService = getRetrofit().create(ApiService::class.java)
        val call = apiService.getData(id0,numberPeople, scheduleAvailable, location, ventilation)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val response = response.body()

                    if (response.toString() == "0") {
                        println("Eliminado")
                    }
                }else{
                    println("Error en la respuesta:")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("Error en la petición: ${t.message}")
            }
        })
    }

    override fun getItemCount() = espacios.size

    fun updateEspacios(newEspacios: List<Espacio>) {
        espacios = newEspacios
        notifyDataSetChanged()
    }


}