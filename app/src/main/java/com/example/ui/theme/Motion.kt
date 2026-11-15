package com.example.ui.theme

import android.provider.Settings
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

object AisleMotion {
    const val Tap = 100
    const val Exit = 150
    const val Standard = 200
    const val Enter = 300
    const val StaggerDelay = 50
    const val StaggerFast = 35 // stagger mais ágil para listas
    const val ShimmerDuration = 1500
    const val IconRotateDuration = 200
    const val ScalePressDuration = 100
    const val DialogEnter = 200
    const val DialogExit = 150
    const val PageEnter = 320
    const val PageExit = 220
    const val StaggerGroup = 60 // entre grupos de items
    val Easing = FastOutSlowInEasing
    val Linear = LinearEasing

    // Spring orgânico padrão ( leve, "viva")
    fun <T> springSpec() = spring<T>(dampingRatio = 0.78f, stiffness = Spring.StiffnessMediumLow)
    // Spring mais firme para botões (resposta tátil)
    fun <T> springFirm() = spring<T>(dampingRatio = 0.72f, stiffness = Spring.StiffnessMedium)
    // Spring super macio para transições de layout
    fun <T> springSoft() = spring<T>(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
    // Spring de bounce sutil para feedback de ação
    fun <T> springBounce() = spring<T>(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium)
}

/** True quando o usuário desligou as animações do sistema. */
@Composable
fun rememberReducedMotion(): Boolean {
    val context = LocalContext.current
    return remember {
        try {
            Settings.Global.getFloat(
                context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f
            ) == 0f
        } catch (e: Exception) {
            false
        }
    }
}

/**
 * Retorna a spec de animação apropriada respeitando reduceMotion.
 * Se reduceMotion for true, retorna snap (sem animação).
 */
@Composable
fun <T> rememberAnimationSpec(
    reduceMotion: Boolean,
    durationMillis: Int = AisleMotion.Standard,
    easing: androidx.compose.animation.core.Easing = AisleMotion.Easing,
    delayMillis: Int = 0
): AnimationSpec<T> {
    return if (reduceMotion) snap() else tween<T>(durationMillis, delayMillis, easing)
}

/**
 * Helper para animateFloatAsState que respeita reduceMotion.
 */
@Composable
fun animateFloatAsStateWithReduceMotion(
    targetValue: Float,
    reduceMotion: Boolean,
    durationMillis: Int = AisleMotion.Standard,
    easing: androidx.compose.animation.core.Easing = AisleMotion.Easing,
    delayMillis: Int = 0,
    label: String = ""
): androidx.compose.runtime.State<Float> {
    val spec = rememberAnimationSpec<Float>(
        reduceMotion = reduceMotion,
        durationMillis = durationMillis,
        easing = easing,
        delayMillis = delayMillis
    )
    return androidx.compose.animation.core.animateFloatAsState(
        targetValue = targetValue,
        animationSpec = spec,
        label = label
    )
}

/** Specs de enter/exit para AnimatedVisibility que respeitam reduceMotion. */
data class AisleVisibilitySpecs(
    val enter: EnterTransition,
    val exit: ExitTransition
)

/**
 * Helper para AnimatedVisibility enter/exit specs que respeitam reduceMotion.
 * Retorna AisleVisibilitySpecs com enter e exit transitions.
 */
@Composable
fun rememberAnimatedVisibilitySpecs(
    reduceMotion: Boolean,
    enterDuration: Int = AisleMotion.Enter,
    exitDuration: Int = AisleMotion.Exit,
    enterDelay: Int = 0,
    exitDelay: Int = 0
): AisleVisibilitySpecs {
    val enter = if (reduceMotion) {
        EnterTransition.None
    } else {
        fadeIn(tween(enterDuration, enterDelay, AisleMotion.Easing), initialAlpha = 0f)
    }
    val exit = if (reduceMotion) {
        ExitTransition.None
    } else {
        fadeOut(tween(exitDuration, exitDelay, AisleMotion.Easing))
    }
    return AisleVisibilitySpecs(enter, exit)
}