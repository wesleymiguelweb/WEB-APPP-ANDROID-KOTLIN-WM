package com.example.filmesplay

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilmesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filmes)
        NavigationHelper.configurar(this, findViewById(R.id.bottomNavigation), R.id.menu_filmes)
        carregarFilmes()
    }

    private fun carregarFilmes() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewFilmes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitClient.api.getFilmes().enqueue(object : Callback<List<Filme>> {
            override fun onResponse(call: Call<List<Filme>>, response: Response<List<Filme>>) {
                if (response.isSuccessful) {
                    recyclerView.adapter = FilmeAdapter(response.body() ?: emptyList())
                } else {
                    Toast.makeText(this@FilmesActivity, "Erro ao carregar filmes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Filme>>, t: Throwable) {
                Toast.makeText(this@FilmesActivity, "Falha ao carregar filmes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
