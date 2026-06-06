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

class AdminProgramacaoActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_programacao)

        recyclerView = findViewById(R.id.recyclerViewAdminProgramacao)
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnIncluirSessao).setOnClickListener {
            startActivity(Intent(this, IncluirSessaoActivity::class.java))
        }
        carregarProgramacao()
    }

    override fun onResume() {
        super.onResume()
        carregarProgramacao()
    }

    private fun carregarProgramacao() {
        RetrofitClient.api.getProgramacao().enqueue(object : Callback<List<Sessao>> {
            override fun onResponse(call: Call<List<Sessao>>, response: Response<List<Sessao>>) {
                if (response.isSuccessful) {
                    recyclerView.adapter = AdminSessaoAdapter(response.body()?.toMutableList() ?: mutableListOf())
                } else {
                    Toast.makeText(this@AdminProgramacaoActivity, "Erro ao carregar programação", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Sessao>>, t: Throwable) {
                Toast.makeText(this@AdminProgramacaoActivity, "Falha: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
