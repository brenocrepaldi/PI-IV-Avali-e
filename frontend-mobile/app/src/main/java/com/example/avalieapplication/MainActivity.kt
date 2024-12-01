package com.example.avalieapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.avalieapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var disciplineIdFromUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Captura o Intent recebido
        val intent = intent
        val action = intent.action
        val data = intent.data

        if (action == Intent.ACTION_VIEW && data != null) {
            // Extrair o disciplineId da URL
            disciplineIdFromUrl = data.getQueryParameter("disciplineId")
            if (disciplineIdFromUrl != null) {
                Toast.makeText(this, "Discipline ID encontrado na URL: $disciplineIdFromUrl", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Discipline ID não encontrado na URL.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.LoginButton.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                sendToBackend(email, password)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendToBackend(email: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) { // Usar lifecycleScope para gerenciar coroutines
            try {
                val response = makeNetworkRequest(email, password)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        if (responseBody != null) {
                            handleSuccessResponse(responseBody)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Resposta vazia do servidor.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Erro de login: ${response.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun makeNetworkRequest(email: String, password: String): Response {
        val client = OkHttpClient()

        val jsonObject = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = jsonObject.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/login")
            .post(requestBody)
            .build()

        return client.newCall(request).execute() // Executa diretamente no Dispatchers.IO
    }

    private fun handleSuccessResponse(responseBody: String) {
        try {
            val jsonResponse = JSONObject(responseBody)
            val userId = jsonResponse.getString("id")
            val accessToken = jsonResponse.getString("accessToken")
            val refreshToken = jsonResponse.getString("refreshToken")
            val accessLevel = jsonResponse.getInt("access_level")

            // Use o disciplineId da URL se ele não for nulo
            val disciplineId = disciplineIdFromUrl ?: jsonResponse.getString("disciplineId")

            // Armazene os tokens usando SharedPreferences
            val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString("userId", userId)
                putString("accessToken", accessToken)
                putString("refreshToken", refreshToken)
                putInt("accessLevel", accessLevel)
                apply()
            }

            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()

            // Redireciona apenas após sucesso
            val intent = Intent(this, EvaluationActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("accessToken", accessToken)
            intent.putExtra("refreshToken", refreshToken)
            intent.putExtra("accessLevel", accessLevel)
            intent.putExtra("disciplineId", disciplineId) // Passa o disciplineId que veio da URL ou do backend
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao processar resposta: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
