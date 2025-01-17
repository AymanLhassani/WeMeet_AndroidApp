package com.example.wemeet_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EspacioAdapter(private var espacios: List<Espacio>) :
    RecyclerView.Adapter<EspacioAdapter.EspacioView>() {

    class EspacioView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val numberPeople: TextView = itemView.findViewById(R.id.numeroEspacioTextView)
        val schedule: TextView = itemView.findViewById(R.id.scheduleEspacioTextView)
        val location: TextView = itemView.findViewById(R.id.locationEspacioTextView)
        val ventilation: TextView = itemView.findViewById(R.id.ventilationEspacioTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspacioView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_espacio, parent, false)
        return EspacioView(view)
    }

    override fun onBindViewHolder(x : EspacioView, position: Int) {
        val espacio = espacios[position]
        x.numberPeople.text = "Personas que admite: " + espacio.numberPeople.toString() + " Personas"
        x.schedule.text = "Horario disponible: " + espacio.scheduleAvailable
        x.location.text = espacio.location
        if(espacio.ventilation) {
            x.ventilation.text = "Si tiene ventilacion"
        }else{
            x.ventilation.text = "No tiene ventilacion"
        }
    }

    override fun getItemCount() = espacios.size

    fun updateEspacios(newEspacios: List<Espacio>) {
        espacios = newEspacios
        notifyDataSetChanged()
    }
}