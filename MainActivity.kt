package com.example.wemeet_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.converter.scalars.ScalarsConverterFactory

object url {
    const val BASE_URL = "http://10.0.2.2:9000/"
}

interface RegisterApi {
    @POST("application/RegisterApp")
    fun register(@Query("n") username: String, @Query("p") password: String): Call<String>
}

class MainActivity : AppCompatActivity() {

    private lateinit var navigateButton: Button


    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateButton = findViewById(R.id.change_button)

        navigateButton.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)
        }

        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        registerButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            register(username, password)
        }
    }

    private fun register(username: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(RegisterApi::class.java)

        //Log.i("MainActivity", "Nom: $username, Pass: $password")
        api.register(username, password).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val response = response.body()

                    if(response.toString() == "0"){
                        Toast.makeText(this@MainActivity, "Usuario registrado", Toast.LENGTH_LONG).show()
                        val intent3 = Intent(this@MainActivity, MainActivity2::class.java)
                        intent3.putExtra("name", username)
                        intent3.putExtra("password", password)
                        startActivity(intent3)
                    }
                    else if(response.toString() == "-1"){
                        Toast.makeText(this@MainActivity, "Ya existe usuario", Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(this@MainActivity, "Ha ocurrido un error, vuelve a intentarlo", Toast.LENGTH_LONG).show()
                    }

                }
            }
            override fun onFailure(call: Call<String>, t: Throwable){
                    Toast.makeText(this@MainActivity, "Ha ocurrido un error en el servidor: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

    }
}
