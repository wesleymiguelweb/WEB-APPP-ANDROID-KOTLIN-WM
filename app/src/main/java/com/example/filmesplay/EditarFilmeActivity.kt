package com.example.filmesplay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarFilmeActivity : AppCompatActivity() {
    private var filmeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_filme)

        filmeId = intent.getIntExtra("FILME_ID", 0)
        preencherCampos()

        findViewById<Button>(R.id.btnSalvarFilme).setOnClickListener {
            val dados = lerCampos()
            RetrofitClient.api.editarFilme(filmeId, dados[0], dados[1], dados[2], dados[3], dados[4], dados[5], dados[6], dados[7], dados[8])
                .enqueue(callback())
        }
    }

    private fun preencherCampos() {
        setTexto(R.id.etFilmeNome, intent.getStringExtra("FILME_NOME"))
        setTexto(R.id.etFilmeDesc, intent.getStringExtra("FILME_DESC"))
        setTexto(R.id.etFilmeGenero, intent.getStringExtra("FILME_GENERO"))
        setTexto(R.id.etFilmeAno, intent.getIntExtra("FILME_ANO", 0).toString())
        setTexto(R.id.etFilmeImagem, intent.getStringExtra("FILME_IMAGEM"))
        setTexto(R.id.etFilmeAvaliacao, intent.getDoubleExtra("FILME_AVALIACAO", 0.0).toString())
        setTexto(R.id.etFilmeDuracao, intent.getIntExtra("FILME_DURACAO", 0).toString())
        setTexto(R.id.etFilmeDiretor, intent.getStringExtra("FILME_DIRETOR"))
        setTexto(R.id.etFilmePais, intent.getStringExtra("FILME_PAIS"))
    }

    private fun lerCampos() = listOf(
        texto(R.id.etFilmeNome), texto(R.id.etFilmeDesc), texto(R.id.etFilmeGenero),
        numero(R.id.etFilmeAno), texto(R.id.etFilmeImagem), decimal(R.id.etFilmeAvaliacao),
        numero(R.id.etFilmeDuracao), texto(R.id.etFilmeDiretor), texto(R.id.etFilmePais)
    )

    private fun setTexto(id: Int, valor: String?) = findViewById<EditText>(id).setText(valor ?: "")
    private fun texto(id: Int) = findViewById<EditText>(id).text.toString().trim().replace(",", ".")
    private fun numero(id: Int) = texto(id).ifEmpty { "0" }
    private fun decimal(id: Int) = texto(id).ifEmpty { "0.0" }

    private fun callback() = object : Callback<RespostaSimples> {
        override fun onResponse(call: Call<RespostaSimples>, response: Response<RespostaSimples>) {
            if (response.isSuccessful) {
                Toast.makeText(this@EditarFilmeActivity, response.body()?.status ?: "Filme atualizado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@EditarFilmeActivity, response.body()?.error ?: "Erro ao atualizar filme", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<RespostaSimples>, t: Throwable) {
            Toast.makeText(this@EditarFilmeActivity, "Falha de conexão", Toast.LENGTH_SHORT).show()
        }
    }
}
