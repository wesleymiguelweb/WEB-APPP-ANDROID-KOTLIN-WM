package com.example.filmesplay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IncluirProdutoActivity : AppCompatActivity() {
    private lateinit var etNome:   EditText
    private lateinit var etDesc:   EditText
    private lateinit var etPreco:  EditText
    private lateinit var etImagem: EditText
    private lateinit var btnSalvar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incluir_produto)

        etNome   = findViewById(R.id.etNomeProduto)
        etDesc   = findViewById(R.id.etDescProduto)
        etPreco  = findViewById(R.id.etPrecoProduto)
        etImagem = findViewById(R.id.etImagemProduto)
        btnSalvar = findViewById(R.id.btnSalvarInclusao)

        btnSalvar.setOnClickListener {
            val nome   = etNome.text.toString().trim()
            val desc   = etDesc.text.toString().trim()
            val preco  = etPreco.text.toString().trim()
            val imagem = etImagem.text.toString().trim()

            if (nome.isEmpty() || desc.isEmpty() || preco.isEmpty()) {
                Toast.makeText(this, "Preencha nome, descrição e preço", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Garante que o preço use ponto em vez de vírgula para o banco de dados
            val precoNormalizado = preco.replace(",", ".")

            btnSalvar.isEnabled = false

            RetrofitClient.api.incluirProduto(nome, desc, precoNormalizado, imagem)
                .enqueue(object : Callback<RespostaSimples> {
                    override fun onResponse(
                        call: Call<RespostaSimples>,
                        response: Response<RespostaSimples>
                    ) {
                        btnSalvar.isEnabled = true
                        val corpo = response.body()
                        if (response.isSuccessful && corpo?.status != null) {
                            Toast.makeText(
                                this@IncluirProdutoActivity,
                                corpo.status,
                                Toast.LENGTH_SHORT
                            ).show()
                            // finish() fecha essa tela e volta para a MainActivity
                            finish()
                        } else {
                            val msg = corpo?.error ?: "Erro ao incluir"
                            Toast.makeText(this@IncluirProdutoActivity, msg, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<RespostaSimples>, t: Throwable) {
                        btnSalvar.isEnabled = true
                        Toast.makeText(
                            this@IncluirProdutoActivity,
                            "Falha de conexão: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }
}