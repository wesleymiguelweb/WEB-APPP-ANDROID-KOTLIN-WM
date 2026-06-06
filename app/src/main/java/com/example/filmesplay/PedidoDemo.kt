package com.example.filmesplay

object PedidoDemo {
    const val VALOR_INGRESSO_PADRAO = 25.0

    var filmeNome: String = ""
    var sessaoResumo: String = ""
    var sessaoPreco: Double = 0.0
    val produtos = mutableListOf<ItemPedido>()

    fun produtoSelecionado(id: Int) = produtos.any { it.id == id }

    fun alternarProduto(produto: Produto): Boolean {
        val existente = produtos.indexOfFirst { it.id == produto.PRODUTO_ID }
        if (existente != -1) {
            produtos.removeAt(existente)
            return false
        }

        produtos.add(
            ItemPedido(
                produto.PRODUTO_ID,
                produto.PRODUTO_NOME ?: "Produto",
                produto.PRODUTO_PRECO
            )
        )
        return true
    }

    fun totalProdutos() = produtos.sumOf { it.preco }
    fun totalPedido() = sessaoPreco + totalProdutos()

    fun limpar() {
        filmeNome = ""
        sessaoResumo = ""
        sessaoPreco = 0.0
        produtos.clear()
    }
}

data class ItemPedido(
    val id: Int,
    val nome: String,
    val preco: Double
)
