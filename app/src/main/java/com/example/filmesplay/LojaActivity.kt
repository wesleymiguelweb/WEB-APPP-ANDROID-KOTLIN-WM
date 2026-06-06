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

class LojaActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loja)
        NavigationHelper.configurar(this, findViewById(R.id.bottomNavigation), R.id.menu_loja)

        recyclerView = findViewById(R.id.recyclerViewLoja)
        recyclerView.layoutManager = LinearLayoutManager(this)
        findViewById<Button>(R.id.btnFinalizarPedido).setOnClickListener {
            startActivity(Intent(this, ObrigadoActivity::class.java))
        }
        carregarProdutos()
    }

    private fun carregarProdutos() {
        RetrofitClient.api.getProdutos().enqueue(object : Callback<List<Produto>> {
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {
                val lista = response.body()?.toMutableList() ?: mutableListOf()
                recyclerView.adapter = CustomAdapter(lista, false)
            }

            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                Toast.makeText(this@LojaActivity, "Falha ao carregar produtos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
