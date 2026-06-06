package com.example.filmesplay

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ObrigadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_obrigado)

        val filme = PedidoDemo.filmeNome.ifEmpty { "Nenhum filme selecionado" }
        val sessao = PedidoDemo.sessaoResumo.ifEmpty { "Nenhuma sessão escolhida" }
        val ingresso = if (PedidoDemo.sessaoPreco > 0.0) {
            "R$ ${"%.2f".format(PedidoDemo.sessaoPreco)}"
        } else {
            "R$ 0.00"
        }
        val produtos = if (PedidoDemo.produtos.isEmpty()) {
            "Nenhum produto selecionado"
        } else {
            PedidoDemo.produtos.joinToString("\n") {
                "- ${it.nome}: R$ ${"%.2f".format(it.preco)}"
            }
        }
        val total = PedidoDemo.totalPedido()

        findViewById<TextView>(R.id.tvResumoPedido).text =
            "INGRESSO FILMESPLAY\n\n" +
                "Filme: $filme\n\n" +
                "Sessão escolhida:\n$sessao\n\n" +
                "Ingresso: $ingresso\n\n" +
                "Produtos:\n$produtos\n\n" +
                "Total: R$ ${"%.2f".format(total)}\n\n" +
                "Pagamento demonstrativo. Nenhum dado financeiro foi solicitado."

        findViewById<Button>(R.id.btnVoltarInicio).setOnClickListener {
            PedidoDemo.limpar()
            finish()
        }
    }
}
