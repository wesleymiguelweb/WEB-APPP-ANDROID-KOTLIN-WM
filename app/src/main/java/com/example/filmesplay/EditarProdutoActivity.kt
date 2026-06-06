package com.example.filmesplay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarProdutoActivity : AppCompatActivity() {

    private lateinit var etNome:   EditText
    private lateinit var etDesc:   EditText
    private lateinit var etPreco:  EditText
    private lateinit var etImagem: EditText
    private lateinit var btnSalvar: Button

    private var produtoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_produto)

        etNome   = findViewById(R.id.etNomeEditar)
        etDesc   = findViewById(R.id.etDescEditar)
        etPreco  = findViewById(R.id.etPrecoEditar)
        etImagem = findViewById(R.id.etImagemEditar)
        btnSalvar = findViewById(R.id.btnSalvarEdicao)

        // Recupera os dados que o Adapter enviou via Intent
        // e pré-preenche os campos para o usuário editar
        produtoId = intent.getIntExtra("PRODUTO_ID", 0)
        etNome.setText(intent.getStringExtra("PRODUTO_NOME"))
        etDesc.setText(intent.getStringExtra("PRODUTO_DESC"))
        // PRODUTO_PRECO é Double no Intent, convertemos para String no campo
        etPreco.setText(intent.getDoubleExtra("PRODUTO_PRECO", 0.0).toString())
        etImagem.setText(intent.getStringExtra("PRODUTO_IMAGEM"))

        btnSalvar.setOnClickListener {
            val nome   = etNome.text.toString().trim()
            val desc   = etDesc.text.toString().trim()
            val preco  = etPreco.text.toString().trim()
            val imagem = etImagem.text.toString().trim()

            if (nome.isEmpty() || desc.isEmpty() || preco.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSalvar.isEnabled = false

            // Garante que o preço use ponto em vez de vírgula para o banco de dados
            val precoNormalizado = preco.replace(",", ".")

            RetrofitClient.api.editarProduto(produtoId, nome, desc, precoNormalizado, imagem)
                .enqueue(object : Callback<RespostaSimples> {
                    override fun onResponse(
                        call: Call<RespostaSimples>,
                        response: Response<RespostaSimples>
                    ) {
                        btnSalvar.isEnabled = true
                        val corpo = response.body()
                        
                        // O seu PHP retorna um texto em 'status' quando dá certo
                        if (response.isSuccessful && corpo?.status != null) {
                            Toast.makeText(
                                this@EditarProdutoActivity,
                                corpo.status, // Exibe: "Produto atualizado com sucesso"
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            // Se der erro, o seu PHP preenche o campo 'error'
                            val erro = corpo?.error ?: "Erro ao salvar"
                            Toast.makeText(this@EditarProdutoActivity, erro, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<RespostaSimples>, t: Throwable) {
                        btnSalvar.isEnabled = true
                        Toast.makeText(
                            this@EditarProdutoActivity,
                            "Falha de conexão: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }
}