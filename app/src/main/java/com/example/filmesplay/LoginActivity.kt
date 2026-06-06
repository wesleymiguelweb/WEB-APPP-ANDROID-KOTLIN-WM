package com.example.filmesplay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            // Lê e já valida aqui, sem duplicar em outro método
            val email = emailEditText.text.toString().trim()
            val senha = passwordEditText.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fazerLogin(email, senha)
        }
        
        // Navega para a tela de registro usando um Intent simples.
        findViewById<TextView>(R.id.tvCriarConta).setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }


    private fun fazerLogin(email: String, senha: String) {
        loginButton.isEnabled = false // evita clique duplo enquanto aguarda

        RetrofitClient.api.login(email, senha).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                loginButton.isEnabled = true

                val corpo = response.body()

                if (response.isSuccessful && corpo != null && corpo.sucesso) {
                    // Login OK — libera a área administrativa.
                    val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                    intent.putExtra("NOME_USUARIO", corpo.nome)
                    startActivity(intent)
                    finish()

                } else {
                    // Mostra a mensagem exata que o PHP enviou
                    val msg = corpo?.mensagem ?: "Usuário ou senha inválidos"
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginButton.isEnabled = true
                // onFailure significa que não chegou resposta nenhuma do servidor
                Toast.makeText(
                    this@LoginActivity, "Falha de conexão: ${t.message}", Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
