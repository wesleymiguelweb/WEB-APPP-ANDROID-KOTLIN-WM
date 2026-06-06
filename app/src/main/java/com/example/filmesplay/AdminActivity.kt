package com.example.filmesplay

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        findViewById<Button>(R.id.btnAdminProdutos).setOnClickListener {
            startActivity(Intent(this, AdminProdutosActivity::class.java))
        }
        findViewById<Button>(R.id.btnAdminFilmes).setOnClickListener {
            startActivity(Intent(this, AdminFilmesActivity::class.java))
        }
        findViewById<Button>(R.id.btnAdminProgramacao).setOnClickListener {
            startActivity(Intent(this, AdminProgramacaoActivity::class.java))
        }
    }
}
