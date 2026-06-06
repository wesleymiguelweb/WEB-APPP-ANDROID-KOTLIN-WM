package com.example.filmesplay

import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView

object NavigationHelper {
    fun configurar(activity: Activity, menu: BottomNavigationView, itemAtual: Int) {
        menu.selectedItemId = itemAtual

        menu.setOnItemSelectedListener { item ->
            if (item.itemId == itemAtual) return@setOnItemSelectedListener true

            val destino = when (item.itemId) {
                R.id.menu_inicio -> MainActivity::class.java
                R.id.menu_filmes -> FilmesActivity::class.java
                R.id.menu_programacao -> ProgramacaoActivity::class.java
                R.id.menu_loja -> LojaActivity::class.java
                else -> null
            }

            if (destino != null) {
                activity.startActivity(Intent(activity, destino))
                true
            } else {
                false
            }
        }
    }
}
