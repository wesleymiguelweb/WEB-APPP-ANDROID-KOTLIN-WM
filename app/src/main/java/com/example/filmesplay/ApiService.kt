package com.example.filmesplay

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("senha") senha: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("registro.php")
    fun registrar(
        @Field("nome")  nome: String,
        @Field("email") email: String,
        @Field("senha") senha: String
    ): Call<LoginResponse>

    @GET("produtos.php")
    fun getProdutos(): Call<List<Produto>>

    @GET("filmes.php")
    fun getFilmes(): Call<List<Filme>>

    @GET("programacao.php")
    fun getProgramacao(): Call<List<Sessao>>

    @FormUrlEncoded
    @POST("incluir_filme.php")
    fun incluirFilme(
        @Field("FILME_NOME") nome: String,
        @Field("FILME_DESC") desc: String,
        @Field("FILME_GENERO") genero: String,
        @Field("FILME_ANO") ano: String,
        @Field("FILME_IMAGEM") imagem: String,
        @Field("FILME_AVALIACAO") avaliacao: String,
        @Field("FILME_DURACAO") duracao: String,
        @Field("FILME_DIRETOR") diretor: String,
        @Field("FILME_PAIS") pais: String
    ): Call<RespostaSimples>

    @FormUrlEncoded
    @POST("editar_filme.php")
    fun editarFilme(
        @Field("FILME_ID") id: Int,
        @Field("FILME_NOME") nome: String,
        @Field("FILME_DESC") desc: String,
        @Field("FILME_GENERO") genero: String,
        @Field("FILME_ANO") ano: String,
        @Field("FILME_IMAGEM") imagem: String,
        @Field("FILME_AVALIACAO") avaliacao: String,
        @Field("FILME_DURACAO") duracao: String,
        @Field("FILME_DIRETOR") diretor: String,
        @Field("FILME_PAIS") pais: String
    ): Call<RespostaSimples>

    @FormUrlEncoded
    @POST("deletar_filme.php")
    fun deletarFilme(@Field("FILME_ID") id: Int): Call<RespostaSimples>

    @FormUrlEncoded
    @POST("incluir_programacao.php")
    fun incluirSessao(
        @Field("FILME_NOME") filme: String,
        @Field("SESSAO_SALA") sala: String,
        @Field("SESSAO_AUDIO") audio: String,
        @Field("SESSAO_FORMATO") formato: String,
        @Field("SESSAO_HORARIOS") horarios: String
    ): Call<RespostaSimples>

    @FormUrlEncoded
    @POST("editar_programacao.php")
    fun editarSessao(
        @Field("SESSAO_ID") id: Int,
        @Field("FILME_NOME") filme: String,
        @Field("SESSAO_SALA") sala: String,
        @Field("SESSAO_AUDIO") audio: String,
        @Field("SESSAO_FORMATO") formato: String,
        @Field("SESSAO_HORARIOS") horarios: String
    ): Call<RespostaSimples>

    @FormUrlEncoded
    @POST("deletar_programacao.php")
    fun deletarSessao(@Field("SESSAO_ID") id: Int): Call<RespostaSimples>

    @FormUrlEncoded
    @POST("incluir_produto.php")
    fun incluirProduto(
        @Field("PRODUTO_NOME")   nome: String,
        @Field("PRODUTO_DESC")   descricao: String,
        @Field("PRODUTO_PRECO")  preco: String,
        @Field("PRODUTO_IMAGEM") imagem: String
    ): Call<RespostaSimples>

    @FormUrlEncoded
    @POST("editar_produto.php")
    fun editarProduto(
        @Field("PRODUTO_ID")     id: Int,
        @Field("PRODUTO_NOME")   nome: String,
        @Field("PRODUTO_DESC")   descricao: String,
        @Field("PRODUTO_PRECO")  preco: String,
        @Field("PRODUTO_IMAGEM") imagem: String
    ): Call<RespostaSimples>

    @FormUrlEncoded
    @POST("deletar_produto.php")
    fun deletarProduto(
        @Field("PRODUTO_ID") id: Int
    ): Call<RespostaSimples>
}
