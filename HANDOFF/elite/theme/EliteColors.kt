package com.example.aisle.ui.theme // AJUSTE para o pacote real do projeto (veja Color.kt)

import androidx.compose.ui.graphics.Color

/** Paleta Elite. Valores medidos do protótipo (HANDOFF/elite/html). Tema escuro profissional, só escuro. */
object EliteColors {
    // Superfícies
    val Background   = Color(0xFF0A0B09) // fundo das telas e cor base do scrim do mapa
    val Surface      = Color(0xFF10120D) // barra inferior, bottom sheets, base do "vidro"
    val Card         = Color(0xFF161911)
    val CardElevated = Color(0xFF1D2116) // arco do menu radial (92%), trilhas, item inativo
    val Field        = Color(0xFF0D0F0A)
    val Line         = Color(0x1AECE8DA) // 10%: bordas de 1 px e divisores
    val LineStrong   = Color(0x2EECE8DA) // 18%: bordas de campo, sheet, botão fantasma

    // Texto
    val Text         = Color(0xFFECE8DA) // osso
    val TextMuted    = Color(0xFF8F9384)
    val TextDim      = Color(0xFF5E6255) // desabilitado

    // Ação e destaque
    val Olive        = Color(0xFFA3B565) // seleção, Pausar, abas ativas, progresso
    val OliveDark    = Color(0xFF5D6B34) // switch ligado, bordas de chip ativo
    val OliveSoft    = Color(0x24A3B565) // fundo de chip/ícone ativo (14%)
    val OnOlive      = Color(0xFF0E1306)
    val Red          = Color(0xFFD12B2B) // Começar treino, Parar, destrutivo
    val RedSoft      = Color(0x26D12B2B)
    val OnRed        = Color(0xFFFFF4F0)
    val Brass        = Color(0xFFC9A24B) // conquistas e patentes
    val Amber        = Color(0xFFE0A93A) // avisos e pausa
    val Ok           = Color(0xFF7FB069) // GPS ativo, ponto verde

    // Glass (backdrop blur não é reproduzível em Compose: usar só o fundo)
    val Glass        = Color(0xD110120D) // 82%
    val GlassBar     = Color(0xE610120D) // 90%: barra de ações da corrida
    val StepRest     = Color(0xFF4A4E40) // blocos de "caminhar" no gráfico de intensidade
}

/** Paradas do degradê de cima sobre o mapa (fração da altura da tela -> alpha), cor base Background. */
val EliteTopScrimStops = arrayOf(
    0.00f to 0.96f, 0.30f to 0.94f, 0.43f to 0.90f, 0.53f to 0.00f, 0.76f to 0.00f, 1.00f to 0.85f,
) // Brush.verticalGradient(colorStops = ... .map { it.first to EliteColors.Background.copy(alpha = it.second) })
