package com.example.filmesplay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IncluirFilmeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_filme)

        findViewById<Button>(R.id.btnSalvarFilme).setOnClickListener {
            val dados = lerCampos()
            if (dados[0].isEmpty() || dados[1].isEmpty()) {
                Toast.makeText(this, "Preencha nome e descrição", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RetrofitClient.api.incluirFilme(dados[0], dados[1], dados[2], dados[3], dados[4], dados[5], dados[6], dados[7], dados[8])
                .enqueue(callback())
        }
    }

    private fun lerCampos() = listOf(
        texto(R.id.etFilmeNome), texto(R.id.etFilmeDesc), texto(R.id.etFilmeGenero),
        numero(R.id.etFilmeAno), texto(R.id.etFilmeImagem), decimal(R.id.etFilmeAvaliacao),
        numero(R.id.etFilmeDuracao), texto(R.id.etFilmeDiretor), texto(R.id.etFilmePais)
    )

    private fun texto(id: Int) = findViewById<EditText>(id).text.toString().trim().replace(",", ".")
    private fun numero(id: Int) = texto(id).ifEmpty { "0" }
    private fun decimal(id: Int) = texto(id).ifEmpty { "0.0" }

    private fun callback() = object : Callback<RespostaSimples> {
        override fun onResponse(call: Call<RespostaSimples>, response: Response<RespostaSimples>) {
            if (response.isSuccessful) {
                Toast.makeText(this@IncluirFilmeActivity, response.body()?.status ?: "Filme salvo", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@IncluirFilmeActivity, response.body()?.error ?: "Erro ao salvar filme", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<RespostaSimples>, t: Throwable) {
            Toast.makeText(this@IncluirFilmeActivity, "Falha de conexão", Toast.LENGTH_SHORT).show()
        }
    }
}
