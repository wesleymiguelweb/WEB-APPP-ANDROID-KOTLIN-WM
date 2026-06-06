package com.example.filmesplay

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val sucesso: Boolean,
    val mensagem: String?,
    val nome: String?,
    
    // Mantendo os campos anteriores caso sejam usados em outras partes do app
    val usuarioId: Int? = null,
    @SerializedName("usuarioNome") val usuarioNome: String? = null,
    val usuarioEmail: String? = null,
    val usuarioCpf: String? = null
)