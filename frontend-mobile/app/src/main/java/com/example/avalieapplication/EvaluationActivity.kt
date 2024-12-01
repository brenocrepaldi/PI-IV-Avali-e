package com.example.avalieapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.avalieapplication.databinding.ActivityEvaluationBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EvaluationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEvaluationBinding // Variável para o ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEvaluationBinding.inflate(layoutInflater)
        setContentView(binding.root) // Usa o root do binding como conteúdo da Activity

        // Ajuste para o padding do sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar ação ao clicar no botão de enviar
        binding.SubmitButton.setOnClickListener {
            val rating = binding.GeneralRatingBar.rating.toInt()
            val comment = binding.EvaluationMultiText.text.toString()
            val student = intent.getStringExtra("userId") // Recebe id do aluno vindo de Login
            val accessToken = intent.getStringExtra("accessToken")
            val disciplineId = intent.getStringExtra("disciplineId") // Recebe id da disciplina vindo de Login

            if (student != null && comment.isNotEmpty() && rating > 0 && accessToken != null && disciplineId != null) {
                sendToBackend(comment, rating, student, accessToken, disciplineId)
            } else {
                Toast.makeText(
                    this,
                    "Por favor, preencha o comentário e avalie com pelo menos 1 estrela.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendToBackend(comment: String, rating: Int, student: String, accessToken: String, disciplineId: String) {
        val client = OkHttpClient()

        val jsonObject = JSONObject().apply {
            put("text", comment)
            put("student", student)  // Passando o ID do aluno
            put("discipline", disciplineId) // ID da disciplina
            put("note", rating)
            put("date", getCurrentDate()) // Adiciona a data atual
        }

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = jsonObject.toString().toRequestBody(mediaType)

        // URL do backend para o envio
        val url = "http://10.0.2.2:8080/feedback/register"

        // Adicionando o token no cabeçalho da requisição
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .post(requestBody)
            .build()

        // Enviar a requisição
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@EvaluationActivity,
                        "Erro ao enviar avaliação: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@EvaluationActivity,
                            "Avaliação enviada com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            finish() // Isso fecha a atividade após 5 segundos
                        }, 2000)
                    } else {
                        // Exibir o código de resposta e a mensagem do erro
                        Log.e("Avaliação", "Erro: ${response.code} - ${response.message}")
                        response.body?.let {
                            val errorBody = it.string()
                            Log.e("Avaliação", "Detalhes do erro: $errorBody")
                        }
                        Toast.makeText(
                            this@EvaluationActivity,
                            "Erro: Código ${response.code} - ${response.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()) // Sem os milissegundos
        return dateFormat.format(Date())
    }
}
