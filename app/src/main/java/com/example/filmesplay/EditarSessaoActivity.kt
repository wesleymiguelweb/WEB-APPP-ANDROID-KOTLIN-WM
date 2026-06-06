package com.example.filmesplay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarSessaoActivity : AppCompatActivity() {
    private var sessaoId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_sessao)

        sessaoId = intent.getIntExtra("SESSAO_ID", 0)
        setTexto(R.id.etSessaoFilme, intent.getStringExtra("FILME_NOME"))
        setTexto(R.id.etSessaoSala, intent.getStringExtra("SESSAO_SALA"))
        setTexto(R.id.etSessaoAudio, intent.getStringExtra("SESSAO_AUDIO"))
        setTexto(R.id.etSessaoFormato, intent.getStringExtra("SESSAO_FORMATO"))
        setTexto(R.id.etSessaoHorarios, intent.getStringExtra("SESSAO_HORARIOS"))

        findViewById<Button>(R.id.btnSalvarSessao).setOnClickListener {
            RetrofitClient.api.editarSessao(
                sessaoId,
                texto(R.id.etSessaoFilme),
                texto(R.id.etSessaoSala),
                texto(R.id.etSessaoAudio),
                texto(R.id.etSessaoFormato),
                texto(R.id.etSessaoHorarios)
            ).enqueue(callback())
        }
    }

    private fun setTexto(id: Int, valor: String?) = findViewById<EditText>(id).setText(valor ?: "")
    private fun texto(id: Int) = findViewById<EditText>(id).text.toString().trim()

    private fun callback() = object : Callback<RespostaSimples> {
        override fun onResponse(call: Call<RespostaSimples>, response: Response<RespostaSimples>) {
            if (response.isSuccessful) {
                Toast.makeText(this@EditarSessaoActivity, response.body()?.status ?: "Sessão atualizada", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@EditarSessaoActivity, response.body()?.error ?: "Erro ao atualizar sessão", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<RespostaSimples>, t: Throwable) {
            Toast.makeText(this@EditarSessaoActivity, "Falha de conexão", Toast.LENGTH_SHORT).show()
        }
    }
}
