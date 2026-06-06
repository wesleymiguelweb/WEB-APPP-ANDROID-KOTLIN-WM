package com.example.filmesplay

data class Produto(
    val PRODUTO_ID: Int,
    val PRODUTO_NOME: String?,
    val PRODUTO_DESC: String?,
    val PRODUTO_PRECO: Double, // Mudado para Double
    val PRODUTO_IMAGEM: String?
)