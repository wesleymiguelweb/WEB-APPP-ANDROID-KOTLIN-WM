package com.example.filmesplay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminFilmesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_filmes)

        recyclerView = findViewById(R.id.recyclerViewAdminFilmes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnIncluirFilme).setOnClickListener {
            startActivity(Intent(this, IncluirFilmeActivity::class.java))
        }
        carregarFilmes()
    }

    override fun onResume() {
        super.onResume()
        carregarFilmes()
    }

    private fun carregarFilmes() {
        RetrofitClient.api.getFilmes().enqueue(object : Callback<List<Filme>> {
            override fun onResponse(call: Call<List<Filme>>, response: Response<List<Filme>>) {
                if (response.isSuccessful) {
                    recyclerView.adapter = AdminFilmeAdapter(response.body()?.toMutableList() ?: mutableListOf())
                } else {
                    Toast.makeText(this@AdminFilmesActivity, "Erro ao carregar filmes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Filme>>, t: Throwable) {
                Toast.makeText(this@AdminFilmesActivity, "Falha: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
