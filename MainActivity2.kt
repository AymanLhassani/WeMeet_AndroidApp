package com.example.wemeet_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query


object url2 {
    const val BASE_URL = "http://10.0.2.2:9000/"
}

interface LoginApi {
    @POST("application/LoginApp")  // Asegúrate de que el endpoint sea correcto en tu servidor
    fun login(@Query("n") username: String, @Query("p") password: String): Call<String>
}

class MainActivity2 : AppCompatActivity() {


    private lateinit var navigateButton: Button

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        navigateButton = findViewById(R.id.change_button)

        navigateButton.setOnClickListener {
            val intent = Intent(this@MainActivity2, MainActivity::class.java)
            startActivity(intent)
        }

        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            login(username, password)
        }
    }

    private fun login(username: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(url2.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(LoginApi::class.java)

        api.login(username, password).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response.toString() == "0") {
                        Toast.makeText(this@MainActivity2, "Has iniciado sesion!", Toast.LENGTH_LONG).show()
                    } else if(response.toString() == "-1") {
                        Toast.makeText(this@MainActivity2, "Contraseña o usuario incorrectos!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity2, "Ha ocurrido un error, vuelve a intentarlo", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@MainActivity2, "Ha ocurrido un error con el servidor: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}