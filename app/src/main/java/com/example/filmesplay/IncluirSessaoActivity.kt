package com.example.filmesplay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IncluirSessaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_sessao)

        findViewById<Button>(R.id.btnSalvarSessao).setOnClickListener {
            val filme = texto(R.id.etSessaoFilme)
            val sala = texto(R.id.etSessaoSala)
            val audio = texto(R.id.etSessaoAudio)
            val formato = texto(R.id.etSessaoFormato)
            val horarios = texto(R.id.etSessaoHorarios)

            if (filme.isEmpty() || sala.isEmpty() || horarios.isEmpty()) {
                Toast.makeText(this, "Preencha filme, sala e horários", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RetrofitClient.api.incluirSessao(filme, sala, audio, formato, horarios).enqueue(callback())
        }
    }

    private fun texto(id: Int) = findViewById<EditText>(id).text.toString().trim()

    private fun callback() = object : Callback<RespostaSimples> {
        override fun onResponse(call: Call<RespostaSimples>, response: Response<RespostaSimples>) {
            if (response.isSuccessful) {
                Toast.makeText(this@IncluirSessaoActivity, response.body()?.status ?: "Sessão salva", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@IncluirSessaoActivity, response.body()?.error ?: "Erro ao salvar sessão", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<RespostaSimples>, t: Throwable) {
            Toast.makeText(this@IncluirSessaoActivity, "Falha de conexão", Toast.LENGTH_SHORT).show()
        }
    }
}
