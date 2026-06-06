package com.example.filmesplay

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminSessaoAdapter(private val lista: MutableList<Sessao>) : RecyclerView.Adapter<AdminSessaoAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.adminTitulo)
        val subtitulo: TextView = itemView.findViewById(R.id.adminSubtitulo)
        val editar: Button = itemView.findViewById(R.id.btnEditarAdmin)
        val deletar: Button = itemView.findViewById(R.id.btnDeletarAdmin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_simples, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sessao = lista[position]
        holder.titulo.text = sessao.FILME_NOME
        holder.subtitulo.text = "${sessao.SESSAO_SALA} | ${sessao.SESSAO_AUDIO} | ${sessao.SESSAO_HORARIOS}"

        holder.editar.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditarSessaoActivity::class.java)
            intent.putExtra("SESSAO_ID", sessao.SESSAO_ID)
            intent.putExtra("FILME_NOME", sessao.FILME_NOME)
            intent.putExtra("SESSAO_SALA", sessao.SESSAO_SALA)
            intent.putExtra("SESSAO_AUDIO", sessao.SESSAO_AUDIO)
            intent.putExtra("SESSAO_FORMATO", sessao.SESSAO_FORMATO)
            intent.putExtra("SESSAO_HORARIOS", sessao.SESSAO_HORARIOS)
            holder.itemView.context.startActivity(intent)
        }

        holder.deletar.setOnClickListener {
            RetrofitClient.api.deletarSessao(sessao.SESSAO_ID).enqueue(object : Callback<RespostaSimples> {
                override fun onResponse(call: Call<RespostaSimples>, response: Response<RespostaSimples>) {
                    val pos = lista.indexOfFirst { it.SESSAO_ID == sessao.SESSAO_ID }
                    if (response.isSuccessful && pos != -1) {
                        lista.removeAt(pos)
                        notifyItemRemoved(pos)
                        Toast.makeText(holder.itemView.context, "Sessão removida", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(holder.itemView.context, response.body()?.error ?: "Erro ao deletar sessão", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RespostaSimples>, t: Throwable) {
                    Toast.makeText(holder.itemView.context, "Falha de conexão", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount() = lista.size
}
