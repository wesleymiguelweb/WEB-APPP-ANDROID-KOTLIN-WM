package com.example.filmesplay

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FilmeAdapter(private val lista: List<Filme>) : RecyclerView.Adapter<FilmeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagem: ImageView = itemView.findViewById(R.id.imagemFilme)
        val nome: TextView = itemView.findViewById(R.id.nomeFilme)
        val detalhes: TextView = itemView.findViewById(R.id.detalhesFilme)
        val descricao: TextView = itemView.findViewById(R.id.descricaoFilme)
        val selecionar: Button = itemView.findViewById(R.id.btnSelecionarFilme)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filme_destaque, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filme = lista[position]

        holder.nome.text = filme.FILME_NOME ?: "Filme"
        holder.detalhes.text = montarDetalhes(filme)
        holder.descricao.text = filme.FILME_DESC ?: "Descrição em breve."
        val selecionado = PedidoDemo.filmeNome == filme.FILME_NOME
        atualizarBotao(holder.selecionar, selecionado)
        holder.selecionar.setOnClickListener {
            PedidoDemo.filmeNome = filme.FILME_NOME ?: ""
            atualizarBotao(holder.selecionar, true)
            notifyDataSetChanged()
            Toast.makeText(holder.itemView.context, "Filme selecionado", Toast.LENGTH_SHORT).show()
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetalheFilmeActivity::class.java)
            intent.putExtra("FILME_NOME", filme.FILME_NOME)
            intent.putExtra("FILME_DESC", filme.FILME_DESC)
            intent.putExtra("FILME_GENERO", filme.FILME_GENERO)
            intent.putExtra("FILME_ANO", filme.FILME_ANO ?: 0)
            intent.putExtra("FILME_IMAGEM", filme.FILME_IMAGEM)
            intent.putExtra("FILME_AVALIACAO", filme.FILME_AVALIACAO ?: 0.0)
            intent.putExtra("FILME_DURACAO", filme.FILME_DURACAO ?: 0)
            intent.putExtra("FILME_DIRETOR", filme.FILME_DIRETOR)
            intent.putExtra("FILME_PAIS", filme.FILME_PAIS)
            holder.itemView.context.startActivity(intent)
        }

        if (!filme.FILME_IMAGEM.isNullOrEmpty()) {
            Picasso.get()
                .load(filme.FILME_IMAGEM)
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_placeholder)
                .into(holder.imagem)
        } else {
            holder.imagem.setImageResource(R.drawable.bg_image_placeholder)
        }
    }

    override fun getItemCount() = lista.size

    private fun montarDetalhes(filme: Filme): String {
        val duracao = filme.FILME_DURACAO?.let { "$it min" } ?: "Duração"
        val ano = filme.FILME_ANO?.toString() ?: "Ano"
        val avaliacao = filme.FILME_AVALIACAO?.let { "Nota $it" } ?: "Sem nota"
        return "${filme.FILME_GENERO ?: "Gênero"} | $ano | $duracao | $avaliacao"
    }

    private fun atualizarBotao(botao: Button, selecionado: Boolean) {
        val context = botao.context
        val corFundo = if (selecionado) R.color.cine_green else R.color.cine_gold
        botao.text = if (selecionado) "Adicionado" else "+ Adicionar"
        botao.setTextColor(context.getColor(R.color.cine_bg))
        botao.backgroundTintList = ColorStateList.valueOf(context.getColor(corFundo))
    }
}
