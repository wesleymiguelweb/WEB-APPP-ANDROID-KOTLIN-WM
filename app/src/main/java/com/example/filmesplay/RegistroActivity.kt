package com.example.filmesplay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etEmail: EditText
    private lateinit var etSenha: EditText
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        etNome  = findViewById(R.id.etNome)
        etEmail = findViewById(R.id.etEmailRegistro)
        etSenha = findViewById(R.id.etSenhaRegistro)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        // Botão de registro
        btnRegistrar.setOnClickListener {
            val nome  = etNome.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            // Valida os campos antes de bater na API
            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registrarUsuario(nome, email, senha)
        }

        // "Já tenho conta" volta para o login simplesmente fechando essa tela
        // finish() destrói a RegistroActivity e o Android volta à tela anterior,
        // que é exatamente o LoginActivity — sem precisar de Intent aqui.
        findViewById<TextView>(R.id.tvVoltarLogin).setOnClickListener {
            finish()
        }
    }

    private fun registrarUsuario(nome: String, email: String, senha: String) {
        btnRegistrar.isEnabled = false // evita clique duplo enquanto aguarda

        RetrofitClient.api.registrar(nome, email, senha)
            .enqueue(object : Callback<LoginResponse> {

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    btnRegistrar.isEnabled = true
                    val corpo = response.body()

                    if (response.isSuccessful && corpo != null && corpo.sucesso) {
                        Toast.makeText(
                            this@RegistroActivity,
                            "Conta criada! Faça seu login.",
                            Toast.LENGTH_LONG
                        ).show()
                        // Fecha a tela de registro e volta ao login automaticamente
                        finish()
                    } else {
                        // Se sucesso for false ou houver erro de servidor (ex: 400, 404, 500)
                        val msg = corpo?.mensagem ?: "Erro ao criar conta (Código: ${response.code()})"
                        Toast.makeText(this@RegistroActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    btnRegistrar.isEnabled = true
                    Toast.makeText(
                        this@RegistroActivity,
                        "Falha de conexão: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}