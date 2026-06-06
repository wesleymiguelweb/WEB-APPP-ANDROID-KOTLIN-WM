package com.example.filmesplay

import com.google.gson.annotations.SerializedName

data class RespostaSimples(
    // O seu PHP manda o sucesso no campo 'status' como String
    @SerializedName("status")
    val status: String?,
    
    // O seu PHP manda o erro no campo 'error' como String
    @SerializedName("error")
    val error: String?
)
