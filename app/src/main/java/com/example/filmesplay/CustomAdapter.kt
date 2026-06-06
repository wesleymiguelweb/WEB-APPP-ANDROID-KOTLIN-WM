package com.example.filmesplay

import android.content.Intent
import android.content.res.ColorStateList
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

class CustomAdapter(
    // MutableList permite remover itens sem recarregar a lista toda do servidor
    private val lista: MutableList<Produto>,
    private val exibirAcoes: Boolean = true
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNome:    TextView  = itemView.findViewById(R.id.nomeProduto)
        val tvDesc:    TextView  = itemView.findViewById(R.id.descricaoProduto)
        val tvPreco:   TextView  = itemView.findViewById(R.id.precoProduto)
        val ivImagem:  ImageView = itemView.findViewById(R.id.imagemProduto)
        val btnSelecionar: Button = itemView.findViewById(R.id.btnSelecionarProduto)
        val btnEditar: Button    = itemView.findViewById(R.id.editarButton)
        val btnDeletar: Button   = itemView.findViewById(R.id.deletarButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = lista[position]

        holder.tvNome.text  = produto.PRODUTO_NOME
        holder.tvDesc.text  = produto.PRODUTO_DESC
        holder.tvPreco.text = "R$ ${"%.2f".format(produto.PRODUTO_PRECO)}"
        holder.btnSelecionar.visibility = if (exibirAcoes) View.GONE else View.VISIBLE
        holder.btnEditar.visibility = if (exibirAcoes) View.VISIBLE else View.GONE
        holder.btnDeletar.visibility = if (exibirAcoes) View.VISIBLE else View.GONE
        atualizarBotaoSelecao(holder.btnSelecionar, PedidoDemo.produtoSelecionado(produto.PRODUTO_ID))

        // Picasso carrega a imagem da URL e coloca no ImageView
        // O .placeholder() exibe uma cor enquanto a imagem carrega
        // O .error() exibe algo se a URL falhar
        if (!produto.PRODUTO_IMAGEM.isNullOrEmpty()) {
            Picasso.get()
                .load(produto.PRODUTO_IMAGEM)
                .placeholder(android.R.color.darker_gray)
                .error(android.R.color.holo_red_light)
                .into(holder.ivImagem)
        } else {
            // Limpa a imagem se a URL estiver vazia para não mostrar a imagem de outro item reciclado
            holder.ivImagem.setImageResource(R.drawable.bg_image_placeholder)
        }

        holder.btnSelecionar.setOnClickListener {
            val adicionado = PedidoDemo.alternarProduto(produto)
            atualizarBotaoSelecao(holder.btnSelecionar, adicionado)
            notifyDataSetChanged()
            val mensagem = if (adicionado) "Produto adicionado" else "Produto removido"
            Toast.makeText(holder.itemView.context, mensagem, Toast.LENGTH_SHORT).show()
        }

        // Botão Editar: abre a EditarProdutoActivity passando todos os dados
        // via Intent para que os campos já venham preenchidos
        holder.btnEditar.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditarProdutoActivity::class.java)
            intent.putExtra("PRODUTO_ID",    produto.PRODUTO_ID)
            intent.putExtra("PRODUTO_NOME",  produto.PRODUTO_NOME)
            intent.putExtra("PRODUTO_DESC",  produto.PRODUTO_DESC)
            intent.putExtra("PRODUTO_PRECO", produto.PRODUTO_PRECO)
            intent.putExtra("PRODUTO_IMAGEM", produto.PRODUTO_IMAGEM ?: "")
            holder.itemView.context.startActivity(intent)
        }

        // Botão Deletar: chama a API e, só se der sucesso, remove o item
        holder.btnDeletar.setOnClickListener {
            RetrofitClient.api.deletarProduto(produto.PRODUTO_ID)
                .enqueue(object : Callback<RespostaSimples> {
                    override fun onResponse(
                        call: Call<RespostaSimples>,
                        response: Response<RespostaSimples>
                    ) {
                        val corpo = response.body()
                        if (response.isSuccessful && corpo?.status != null) {
                            // Busca a posição atual do item na lista para remover com segurança
                            val currentPos = lista.indexOfFirst { it.PRODUTO_ID == produto.PRODUTO_ID }
                            if (currentPos != -1) {
                                lista.removeAt(currentPos)
                                notifyItemRemoved(currentPos)
                                Toast.makeText(holder.itemView.context, "Produto deletado!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val erro = corpo?.error ?: "Erro ao deletar"
                            Toast.makeText(holder.itemView.context, erro, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RespostaSimples>, t: Throwable) {
                        Toast.makeText(
                            holder.itemView.context,
                            "Falha de conexão",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    override fun getItemCount() = lista.size

    private fun atualizarBotaoSelecao(botao: Button, selecionado: Boolean) {
        val context = botao.context
        val corFundo = if (selecionado) R.color.cine_green else R.color.cine_gold
        botao.text = if (selecionado) "Remover" else "+ Adicionar"
        botao.setTextColor(context.getColor(R.color.cine_bg))
        botao.backgroundTintList = ColorStateList.valueOf(context.getColor(corFundo))
    }
}
