package com.example.filmesplay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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

class MainActivity : AppCompatActivity() {
    private var filmeDestaque: Filme? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NavigationHelper.configurar(this, findViewById(R.id.bottomNavigation), R.id.menu_inicio)

        val nomeUsuario = intent.getStringExtra("NOME_USUARIO") ?: "Visitante"
        findViewById<TextView>(R.id.tvBoasVindas).text = "Olá, $nomeUsuario"

        configurarListas()
        carregarFilmes()
        carregarProgramacao()

        findViewById<Button>(R.id.btnFilmes).setOnClickListener {
            startActivity(Intent(this, FilmesActivity::class.java))
        }

        findViewById<Button>(R.id.btnProgramacao).setOnClickListener {
            startActivity(Intent(this, ProgramacaoActivity::class.java))
        }

        findViewById<Button>(R.id.btnLoja).setOnClickListener {
            startActivity(Intent(this, LojaActivity::class.java))
        }

        findViewById<Button>(R.id.btnAdmin).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        carregarFilmes()
        carregarProgramacao()
    }

    private fun configurarListas() {
        findViewById<RecyclerView>(R.id.recyclerViewHomeProgramacao).layoutManager = LinearLayoutManager(this)
    }

    private fun carregarFilmes() {
        RetrofitClient.api.getFilmes().enqueue(object : Callback<List<Filme>> {
            override fun onResponse(call: Call<List<Filme>>, response: Response<List<Filme>>) {
                if (response.isSuccessful) {
                    val filmes = response.body() ?: emptyList()
                    if (filmes.isNotEmpty()) {
                        exibirFilmeDestaque(filmes.first())
                        carregarProgramacao()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Erro ao carregar filmes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Filme>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Falha ao carregar filmes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun exibirFilmeDestaque(filme: Filme) {
        filmeDestaque = filme
        findViewById<TextView>(R.id.tvHomeFilmeNome).text = filme.FILME_NOME ?: "Filme em destaque"
        findViewById<TextView>(R.id.tvHomeFilmeDetalhes).text = montarDetalhes(filme)
        findViewById<TextView>(R.id.tvHomeFilmeDesc).text = filme.FILME_DESC ?: "Descrição em breve."

        val imagem = findViewById<ImageView>(R.id.ivHomeFilmeBanner)
        if (!filme.FILME_IMAGEM.isNullOrEmpty()) {
            Picasso.get()
                .load(filme.FILME_IMAGEM)
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_placeholder)
                .into(imagem)
        } else {
            imagem.setImageResource(R.drawable.bg_image_placeholder)
        }

        findViewById<Button>(R.id.btnSelecionarFilmeHome).setOnClickListener {
            PedidoDemo.filmeNome = filme.FILME_NOME ?: ""
            Toast.makeText(this, "Filme selecionado", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.cardFilmeDestaque).setOnClickListener {
            val intent = Intent(this, DetalheFilmeActivity::class.java)
            intent.putExtra("FILME_NOME", filme.FILME_NOME)
            intent.putExtra("FILME_DESC", filme.FILME_DESC)
            intent.putExtra("FILME_GENERO", filme.FILME_GENERO)
            intent.putExtra("FILME_ANO", filme.FILME_ANO ?: 0)
            intent.putExtra("FILME_IMAGEM", filme.FILME_IMAGEM)
            intent.putExtra("FILME_AVALIACAO", filme.FILME_AVALIACAO ?: 0.0)
            intent.putExtra("FILME_DURACAO", filme.FILME_DURACAO ?: 0)
            intent.putExtra("FILME_DIRETOR", filme.FILME_DIRETOR)
            intent.putExtra("FILME_PAIS", filme.FILME_PAIS)
            startActivity(intent)
        }
    }

    private fun montarDetalhes(filme: Filme): String {
        val genero = filme.FILME_GENERO ?: "Gênero"
        val ano = filme.FILME_ANO?.toString() ?: "Ano"
        val duracao = filme.FILME_DURACAO?.let { "$it min" } ?: "Duração"
        val avaliacao = filme.FILME_AVALIACAO?.let { "Nota $it" } ?: "Sem nota"
        return "$genero | $ano | $duracao | $avaliacao"
    }

    private fun carregarProgramacao() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewHomeProgramacao)
        RetrofitClient.api.getProgramacao().enqueue(object : Callback<List<Sessao>> {
            override fun onResponse(call: Call<List<Sessao>>, response: Response<List<Sessao>>) {
                if (response.isSuccessful) {
                    val sessoes = response.body() ?: emptyList()
                    val nomeDestaque = filmeDestaque?.FILME_NOME
                    val sessoesDoDestaque = if (nomeDestaque.isNullOrEmpty()) {
                        sessoes
                    } else {
                        sessoes.filter { it.FILME_NOME == nomeDestaque }
                    }
                    recyclerView.adapter = SessaoAdapter(sessoesDoDestaque)
                } else {
                    Toast.makeText(this@MainActivity, "Erro ao carregar programação", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Sessao>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Falha ao carregar programação", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
