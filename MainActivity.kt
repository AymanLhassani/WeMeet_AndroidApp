package com.example.wemeet_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

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
import android.content.Intent

object url {
    const val BASE_URL = "http://10.0.2.2:9000/"
}

data class RegisterResponse(val success: Boolean, val message: String)

interface RegisterApi {
    @POST("application/RegisterApp")
    fun register(@Query("n") username: String, @Query("p") password: String): Call<RegisterResponse>
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

        api.register(username, password).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null && registerResponse.success) {
                        Toast.makeText(this@MainActivity, "Sign in successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
