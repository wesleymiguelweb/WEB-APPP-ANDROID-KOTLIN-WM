package com.example.filmesplay

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminFilmeAdapter(private val lista: MutableList<Filme>) : RecyclerView.Adapter<AdminFilmeAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagem: ImageView = itemView.findViewById(R.id.adminImagemFilme)
        val nome: TextView = itemView.findViewById(R.id.adminTitulo)
        val desc: TextView = itemView.findViewById(R.id.adminSubtitulo)
        val editar: Button = itemView.findViewById(R.id.btnEditarAdmin)
        val deletar: Button = itemView.findViewById(R.id.btnDeletarAdmin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_filme, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filme = lista[position]
        holder.nome.text = filme.FILME_NOME ?: "Filme"
        holder.desc.text = "${filme.FILME_GENERO ?: "Gênero"} | ${filme.FILME_ANO ?: 0} | ${filme.FILME_DURACAO ?: 0} min"

        if (!filme.FILME_IMAGEM.isNullOrEmpty()) {
            Picasso.get()
                .load(filme.FILME_IMAGEM)
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_placeholder)
                .into(holder.imagem)
        } else {
            holder.imagem.setImageResource(R.drawable.bg_image_placeholder)
        }

        holder.editar.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditarFilmeActivity::class.java)
            preencherIntent(intent, filme)
            holder.itemView.context.startActivity(intent)
        }

        holder.deletar.setOnClickListener {
            RetrofitClient.api.deletarFilme(filme.FILME_ID).enqueue(object : Callback<RespostaSimples> {
                override fun onResponse(call: Call<RespostaSimples>, response: Response<RespostaSimples>) {
                    val pos = lista.indexOfFirst { it.FILME_ID == filme.FILME_ID }
                    if (response.isSuccessful && pos != -1) {
                        lista.removeAt(pos)
                        notifyItemRemoved(pos)
                        Toast.makeText(holder.itemView.context, "Filme removido", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(holder.itemView.context, response.body()?.error ?: "Erro ao deletar filme", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RespostaSimples>, t: Throwable) {
                    Toast.makeText(holder.itemView.context, "Falha de conexão", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun getItemCount() = lista.size

    private fun preencherIntent(intent: Intent, filme: Filme) {
        intent.putExtra("FILME_ID", filme.FILME_ID)
        intent.putExtra("FILME_NOME", filme.FILME_NOME)
        intent.putExtra("FILME_DESC", filme.FILME_DESC)
        intent.putExtra("FILME_GENERO", filme.FILME_GENERO)
        intent.putExtra("FILME_ANO", filme.FILME_ANO ?: 0)
        intent.putExtra("FILME_IMAGEM", filme.FILME_IMAGEM)
        intent.putExtra("FILME_AVALIACAO", filme.FILME_AVALIACAO ?: 0.0)
        intent.putExtra("FILME_DURACAO", filme.FILME_DURACAO ?: 0)
        intent.putExtra("FILME_DIRETOR", filme.FILME_DIRETOR)
        intent.putExtra("FILME_PAIS", filme.FILME_PAIS)
    }
}
