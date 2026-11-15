package com.example

import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.pow

/** Razão de contraste WCAG entre pares texto/fundo dos tokens do Aisle. */
class ContrastRatioTest {

    private fun lum(hex: Long): Double {
        fun ch(v: Int): Double {
            val c = v / 255.0
            return if (c <= 0.03928) c / 12.92 else ((c + 0.055) / 1.055).pow(2.4)
        }
        val r = ((hex shr 16) and 0xFF).toInt()
        val g = ((hex shr 8) and 0xFF).toInt()
        val b = (hex and 0xFF).toInt()
        return 0.2126 * ch(r) + 0.7152 * ch(g) + 0.0722 * ch(b)
    }

    private fun ratio(a: Long, b: Long): Double {
        val l1 = lum(a); val l2 = lum(b)
        return (maxOf(l1, l2) + 0.05) / (minOf(l1, l2) + 0.05)
    }

    private val bg = 0x0B1020L
    private val card = 0x141B2EL

    @Test
    fun `textos comuns tem pelo menos 4,5 para 1`() {
        val pares = mapOf(
            "branco/fundo" to ratio(0xFFFFFF, bg),
            "branco/cartao" to ratio(0xFFFFFF, card),
            "secundario/fundo" to ratio(0x9AA3B8, bg),
            "secundario/cartao" to ratio(0x9AA3B8, card),
            "turquesa/fundo" to ratio(0x19C3B1, bg),
            "turquesa/cartao" to ratio(0x19C3B1, card),
            "roxo claro (texto)/fundo" to ratio(0xA68BFF, bg),
            "roxo claro (texto)/cartao" to ratio(0xA68BFF, card),
            "texto escuro/laranja" to ratio(0x0B1020, 0xFF7A2F),
            "texto escuro/laranja 2" to ratio(0x0B1020, 0xFF5A1F),
            "branco/turquesa escuro" to ratio(0xFFFFFF, 0x0B857A),
            "alerta/fundo" to ratio(0xFF5C5C, bg)
        )
        pares.forEach { (nome, r) -> assertTrue("$nome = %.2f".format(r), r >= 4.5) }
    }

    @Test
    fun `borda de campo tem pelo menos 3 para 1`() {
        assertTrue(ratio(0x6A759E, bg) >= 3.0)
        assertTrue(ratio(0x6A759E, card) >= 3.0)
    }
}
