package com.example.filmesplay

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalheFilmeActivity : AppCompatActivity() {
    private lateinit var nomeFilme: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_filme)

        val imagem = intent.getStringExtra("FILME_IMAGEM")
        nomeFilme = intent.getStringExtra("FILME_NOME") ?: "Filme"
        findViewById<TextView>(R.id.tvDetalheNome).text = nomeFilme
        findViewById<TextView>(R.id.tvDetalheDesc).text = intent.getStringExtra("FILME_DESC") ?: "Descrição em breve."
        findViewById<TextView>(R.id.tvDetalheInfo).text =
            "${intent.getStringExtra("FILME_GENERO") ?: "Gênero"} | ${intent.getIntExtra("FILME_ANO", 0)} | ${intent.getIntExtra("FILME_DURACAO", 0)} min"
        findViewById<TextView>(R.id.tvDetalheEquipe).text =
            "Direção: ${intent.getStringExtra("FILME_DIRETOR") ?: "Não informado"}\nPaís: ${intent.getStringExtra("FILME_PAIS") ?: "Não informado"}\nAvaliação: ${intent.getDoubleExtra("FILME_AVALIACAO", 0.0)}"

        val imageView = findViewById<ImageView>(R.id.ivDetalheImagem)
        if (!imagem.isNullOrEmpty()) {
            Picasso.get().load(imagem).placeholder(R.drawable.bg_image_placeholder).error(R.drawable.bg_image_placeholder).into(imageView)
        } else {
            imageView.setImageResource(R.drawable.bg_image_placeholder)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewDetalheSessoes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        carregarSessoesDoFilme(recyclerView)
    }

    private fun carregarSessoesDoFilme(recyclerView: RecyclerView) {
        RetrofitClient.api.getProgramacao().enqueue(object : Callback<List<Sessao>> {
            override fun onResponse(call: Call<List<Sessao>>, response: Response<List<Sessao>>) {
                if (response.isSuccessful) {
                    val sessoes = response.body()
                        ?.filter { it.FILME_NOME == nomeFilme }
                        ?: emptyList()
                    recyclerView.adapter = SessaoAdapter(sessoes)
                } else {
                    Toast.makeText(this@DetalheFilmeActivity, "Erro ao carregar sessões", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Sessao>>, t: Throwable) {
                Toast.makeText(this@DetalheFilmeActivity, "Falha ao carregar sessões", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
