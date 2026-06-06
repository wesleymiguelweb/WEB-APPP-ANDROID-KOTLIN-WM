package com.example.filmesplay

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class SessaoAdapter(private val lista: List<Sessao>) : RecyclerView.Adapter<SessaoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val filme: TextView = itemView.findViewById(R.id.sessaoFilme)
        val detalhes: TextView = itemView.findViewById(R.id.sessaoDetalhes)
        val horarios: TextView = itemView.findViewById(R.id.sessaoHorarios)
        val preco: TextView = itemView.findViewById(R.id.sessaoPreco)
        val selecionar: Button = itemView.findViewById(R.id.btnSelecionarSessao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sessao, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sessao = lista[position]
        holder.filme.text = sessao.FILME_NOME ?: "Filme"
        val detalhes = "${sessao.SESSAO_SALA ?: "Sala"} | ${sessao.SESSAO_AUDIO ?: "Audio"} | ${sessao.SESSAO_FORMATO ?: "Formato"}"
        holder.detalhes.text = detalhes
        holder.horarios.text = sessao.SESSAO_HORARIOS ?: "Horários em breve"
        holder.preco.text = "Ingresso: R$ ${"%.2f".format(PedidoDemo.VALOR_INGRESSO_PADRAO)}"

        val resumo = "${sessao.FILME_NOME ?: "Filme"} - $detalhes - ${sessao.SESSAO_HORARIOS ?: "Horários em breve"}"
        atualizarBotao(holder.selecionar, PedidoDemo.sessaoResumo == resumo)

        holder.selecionar.setOnClickListener {
            PedidoDemo.sessaoResumo = resumo
            PedidoDemo.sessaoPreco = PedidoDemo.VALOR_INGRESSO_PADRAO
            if (PedidoDemo.filmeNome.isEmpty()) {
                PedidoDemo.filmeNome = sessao.FILME_NOME ?: ""
            }
            atualizarBotao(holder.selecionar, true)
            notifyDataSetChanged()
            Toast.makeText(holder.itemView.context, "Sessão escolhida", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = lista.size

    private fun atualizarBotao(botao: Button, selecionado: Boolean) {
        val context = botao.context
        val corFundo = if (selecionado) R.color.cine_green else R.color.cine_gold
        botao.text = if (selecionado) "Sessão escolhida" else "+ Escolher sessão"
        botao.setTextColor(context.getColor(R.color.cine_bg))
        botao.backgroundTintList = ColorStateList.valueOf(context.getColor(corFundo))
    }
}
