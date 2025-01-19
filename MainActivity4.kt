package com.example.wemeet_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query


interface addSpace {
    @POST("application/addSpaceAndroid")
    fun add(@Query("n") numberPeople: String, @Query("schedule") schedule: String, @Query("location") location: String, @Query("ventilation") ventilation: String, @Query("id") id : Long): Call<String>
}

class MainActivity4 : AppCompatActivity() {


    private lateinit var numberPeopleEditText: EditText
    private lateinit var scheduleAvailable: EditText
    private lateinit var locationEditText: EditText
    private lateinit var ventilationEditText: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)


        numberPeopleEditText = findViewById(R.id.numberPeople)
        scheduleAvailable = findViewById(R.id.scheduleAvailable)
        locationEditText = findViewById(R.id.location)
        ventilationEditText = findViewById(R.id.ventilation)
        addButton = findViewById(R.id.add_button)

        addButton.setOnClickListener {
            val numberPeople = numberPeopleEditText.text.toString()
            val schedule = scheduleAvailable.text.toString()
            val location = locationEditText.text.toString()
            var ventilation = ventilationEditText.text.toString()

            if(ventilation == "Si"){
                ventilation = "true"
            }else{
                ventilation = "false"
            }
            add(numberPeople, schedule, location, ventilation, id0.toLong())
        }
    }
    private fun add(numberPeople: String, schedule: String, location: String, ventilation: String, id: Long) {
        val retrofit = Retrofit.Builder()
            .baseUrl(url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(addSpace::class.java)

        api.add(numberPeople, schedule, location, ventilation, id).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val response = response.body()

                    if(response.toString() == "0"){
                        Toast.makeText(this@MainActivity4, "Espacio añadido", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@MainActivity4, MainActivity5::class.java)
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this@MainActivity4, "Ha ocurrido un error, vuelve a intentarlo", Toast.LENGTH_LONG).show()
                    }

                }
            }
            override fun onFailure(call: Call<String>, t: Throwable){
                Toast.makeText(this@MainActivity4, "Ha ocurrido un error en el servidor: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

    }
}