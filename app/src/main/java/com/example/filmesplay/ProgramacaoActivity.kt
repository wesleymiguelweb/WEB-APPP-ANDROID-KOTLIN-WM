package com.example.filmesplay

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProgramacaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programacao)
        NavigationHelper.configurar(this, findViewById(R.id.bottomNavigation), R.id.menu_programacao)
        carregarProgramacao()
    }

    private fun carregarProgramacao() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProgramacao)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitClient.api.getProgramacao().enqueue(object : Callback<List<Sessao>> {
            override fun onResponse(call: Call<List<Sessao>>, response: Response<List<Sessao>>) {
                if (response.isSuccessful) {
                    recyclerView.adapter = SessaoAdapter(response.body() ?: emptyList())
                } else {
                    Toast.makeText(this@ProgramacaoActivity, "Erro ao carregar programação", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Sessao>>, t: Throwable) {
                Toast.makeText(this@ProgramacaoActivity, "Falha ao carregar programação", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
