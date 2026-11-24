package com.elite.sch.app.ui.components

import android.content.Context
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.window.layout.WindowMetricsCalculator

/**
 * Sistema de layout adaptativo baseado em WindowSizeClass.
 * Fornece helpers para detectar compact/medium/expanded e calcular larguras de conteúdo.
 */

@Composable
fun rememberEliteWindowSizeClass(context: Context = androidx.compose.ui.platform.LocalContext.current): WindowSizeClass {
    val windowMetrics = WindowMetricsCalculator.getOrCreate()
        .computeCurrentWindowMetrics(context)
    val bounds = windowMetrics.bounds

    val density = LocalDensity.current
    val widthDp = with(density) { bounds.width().toDp() }
    val heightDp = with(density) { bounds.height().toDp() }

    return remember(bounds.width(), bounds.height(), density.density) {
        WindowSizeClass.compute(widthDp.value, heightDp.value)
    }
}

/**
 * Largura máxima do conteúdo para telas expandidas (tablets/desktop).
 * Em telas compactas, usa a largura total disponível.
 */
@Composable
fun EliteContentWidth(
    windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass(),
    content: @Composable (maxWidthDp: Float) -> Unit
) {
    BoxWithConstraints {
        val isExpanded = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
        val isMedium = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM

        // Máxima largura do conteúdo em telas grandes (720dp é um breakpoint comum para tablets)
        val maxContentWidthDp = if (isExpanded) {
            720f
        } else if (isMedium) {
            560f
        } else {
            // Em compact, usa toda a largura disponível menos padding padrão
            constraints.maxWidth.toFloat()
        }

        // Limita à largura disponível real
        val effectiveWidth = maxContentWidthDp.coerceAtMost(constraints.maxWidth.toFloat())

        content(effectiveWidth)
    }
}

/**
 * Indica se a tela atual é compacta (telefone em retrato).
 */
@Composable
fun isCompactWindow(windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass()): Boolean {
    return windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
}

/**
 * Indica se a tela atual é média (telefone em paisagem / dobráveis parcialmente abertos).
 */
@Composable
fun isMediumWindow(windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass()): Boolean {
    return windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM
}

/**
 * Indica se a tela atual é expandida (tablet, desktop, dobrável aberto).
 */
@Composable
fun isExpandedWindow(windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass()): Boolean {
    return windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED
}

/**
 * Escala de fonte para o timer de corrida.
 * Em telas grandes, o timer pode ser ainda maior para melhor legibilidade à distância.
 */
@Composable
fun rememberTimerFontScale(
    windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass()
): Float {
    return when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> 1.0f
        WindowWidthSizeClass.MEDIUM -> 1.15f
        WindowWidthSizeClass.EXPANDED -> 1.3f
        else -> 1.0f
    }
}

/**
 * Padding horizontal adaptativo.
 * Compact: 16dp (padrão)
 * Medium: 24dp
 * Expanded: 32dp ou centralizado com margens maiores
 */
@Composable
fun EliteScreenHorizontalPadding(
    windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass()
): androidx.compose.ui.unit.Dp {
    return when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> 16.dp
        WindowWidthSizeClass.MEDIUM -> 24.dp
        WindowWidthSizeClass.EXPANDED -> 32.dp
        else -> 16.dp
    }
}

/**
 * Espaçamento vertical adaptativo entre seções.
 */
@Composable
fun EliteSectionSpacing(
    windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass()
): androidx.compose.ui.unit.Dp {
    return when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> 24.dp // EliteSpacing.xxl
        WindowWidthSizeClass.MEDIUM -> 28.dp
        WindowWidthSizeClass.EXPANDED -> 32.dp
        else -> 24.dp
    }
}

/**
 * Tamanho do botão central de ação adaptativo.
 * Compact: 68dp
 * Medium: 76dp
 * Expanded: 84dp
 */
@Composable
fun EliteCenterActionSize(
    windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass()
): androidx.compose.ui.unit.Dp {
    return when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> 68.dp
        WindowWidthSizeClass.MEDIUM -> 76.dp
        WindowWidthSizeClass.EXPANDED -> 84.dp
        else -> 68.dp
    }
}

/**
 * Altura da barra de navegação inferior adaptativa.
 */
@Composable
fun EliteBottomBarHeight(
    windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass()
): androidx.compose.ui.unit.Dp {
    return when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> 64.dp
        WindowWidthSizeClass.MEDIUM -> 72.dp
        WindowWidthSizeClass.EXPANDED -> 80.dp
        else -> 64.dp
    }
}

/**
 * Tamanho do ícone na barra de navegação inferior.
 */
@Composable
fun EliteBottomNavIconSize(
    windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass()
): androidx.compose.ui.unit.Dp {
    return when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> 22.dp
        WindowWidthSizeClass.MEDIUM -> 24.dp
        WindowWidthSizeClass.EXPANDED -> 28.dp
        else -> 22.dp
    }
}

/**
 * Tamanho do símbolo "A" no botão central.
 */
@Composable
fun EliteCenterSymbolSize(
    windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass()
): androidx.compose.ui.unit.Dp {
    return when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> 30.dp
        WindowWidthSizeClass.MEDIUM -> 34.dp
        WindowWidthSizeClass.EXPANDED -> 38.dp
        else -> 30.dp
    }
}

/**
 * Wrapper para conteúdo que deve ser centralizado e limitado em largura em telas grandes.
 * Em compact, preenche a tela. Em medium/expanded, centraliza com largura máxima.
 */
@Composable
fun EliteAdaptiveContainer(
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    windowSizeClass: WindowSizeClass = rememberEliteWindowSizeClass(),
    content: @Composable (contentWidth: Float) -> Unit
) {
    BoxWithConstraints {
        val isCompact = windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT
        val availableWidth = constraints.maxWidth

        if (isCompact) {
            // Em compact, usa toda a largura
            content(availableWidth.toFloat())
        } else {
            // Em medium/expanded, centraliza com largura máxima
            val maxWidth = when (windowSizeClass.windowWidthSizeClass) {
                WindowWidthSizeClass.MEDIUM -> 560.dp
                WindowWidthSizeClass.EXPANDED -> 720.dp
                else -> availableWidth.dp
            }

            val effectiveWidthDp = maxWidth.coerceAtMost(availableWidth.dp)
            val effectiveWidthPx = LocalDensity.current.run { effectiveWidthDp.toPx() }

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .width(effectiveWidthDp)
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
            ) {
                content(effectiveWidthPx.coerceAtMost(availableWidth.toFloat()))
            }
        }
    }
}

@Composable
fun EliteResponsiveFrame(
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier.fillMaxSize(),
    content: @Composable BoxScope.() -> Unit
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        val contentWidth = when {
            maxWidth < 600.dp -> maxWidth
            maxWidth < 840.dp -> 560.dp
            else -> 720.dp
        }.coerceAtMost(maxWidth)

        Box(
            modifier = androidx.compose.ui.Modifier
                .width(contentWidth)
                .fillMaxHeight(),
            content = content
        )
    }
}