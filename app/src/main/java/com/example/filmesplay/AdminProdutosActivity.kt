package com.example.filmesplay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminProdutosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_produtos)

        recyclerView = findViewById(R.id.recyclerViewProdutos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnIncluirProduto).setOnClickListener {
            startActivity(Intent(this, IncluirProdutoActivity::class.java))
        }

        carregarProdutos()
    }

    override fun onResume() {
        super.onResume()
        carregarProdutos()
    }

    private fun carregarProdutos() {
        RetrofitClient.api.getProdutos().enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                if (response.isSuccessful) {
                    val lista = response.body()?.toMutableList() ?: mutableListOf()
                    recyclerView.adapter = CustomAdapter(lista, true)
                    Log.d("CineApp", "${lista.size} produtos carregados.")
                }
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Log.e("CineApp", "Falha: ${t.message}")
            }
        })
    }
}
