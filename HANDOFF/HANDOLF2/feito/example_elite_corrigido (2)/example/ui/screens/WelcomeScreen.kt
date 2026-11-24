package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AisleButton
import com.example.ui.components.AisleButtonVariant
import com.example.ui.components.AisleLogo
import com.example.ui.components.EmptyState
import com.example.ui.components.LoadingState
import com.example.ui.components.aisleTextFieldColors
import com.example.ui.theme.rememberReducedMotion
import com.example.ui.theme.Background
import com.example.ui.theme.Card
import com.example.ui.theme.EliteBodyFont
import com.example.ui.theme.EliteDisplayFont
import com.example.ui.theme.EliteLogoFont
import com.example.ui.theme.Line
import com.example.ui.theme.Olive
import com.example.ui.theme.OliveSoft
import com.example.ui.theme.OnOlive
import com.example.ui.theme.OnRed
import com.example.ui.theme.Red
import com.example.ui.theme.Text
import com.example.ui.theme.TextMuted
import com.example.R
import com.example.ui.theme.Card as CardColor
import com.example.ui.theme.Text as TextColor

@Composable
fun WelcomeScreen(
    userName: String = "",
    onUserNameChange: (String) -> Unit = {},
    isLoading: Boolean = false,
    isEmpty: Boolean = false,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        LoadingState(message = "Preparando sua jornada...", modifier = modifier)
        return
    }

    if (isEmpty) {
        EmptyState(
            title = "Bem-vindo ao Elite",
            description = "Nenhum dado encontrado no momento.",
            actionLabel = "Começar",
            onActionClick = onStartClick,
            modifier = modifier
        )
        return
    }

    // Nome obrigatório: bloquear avanço se vazio ou só espaços (spec §2)
    val trimmedName = userName.trim()
    val isNameValid = trimmedName.isNotBlank()
    var showNameError by remember { mutableStateOf(false) }

    // Entrada única: fade + subida de 16 dp em 300 ms (sem movimento se reduzido)
    val reducedMotion = rememberReducedMotion()
    var visible by remember { mutableStateOf(reducedMotion) }
    LaunchedEffect(Unit) { visible = true }

    val density = LocalDensity.current
    val slideOffsetPx = remember(density) { with(density) { 16.dp.roundToPx() } }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 56.dp) // padding-top 56dp per spec
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = if (reducedMotion) fadeIn(tween(100))
                    else fadeIn(tween(300)) + slideInVertically(tween(300)) { slideOffsetPx }
                ) {
                    Column {
                        // Logo Elite vertical: símbolo 72x82 + gap 10 + "ELITE" 38sp + tagline 9sp
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Símbolo Elite mark (osso) 72x82
                            androidx.compose.ui.res.painterResource(id = R.drawable.ic_elite_mark)
                                .let { painter ->
                                    androidx.compose.foundation.Image(
                                        painter = painter,
                                        contentDescription = null,
                                        contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                                        modifier = Modifier.width(72.dp).height(82.dp)
                                    )
                                }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "ELITE",
                                    fontFamily = EliteLogoFont,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 38.sp,
                                    lineHeight = 38.sp,
                                    letterSpacing = 9.12.sp, // 0.24em
                                    color = TextColor
                                )
                                Text(
                                    text = "MISSÃO DADA É MISSÃO CUMPRIDA",
                                    fontFamily = EliteBodyFont,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 9.sp,
                                    letterSpacing = 1.8.sp, // 0.13em * 14 ≈ 1.8 (proportional to 14sp base)
                                    color = TextMuted,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(18.dp)) // gap after logo

                        // Título t1: "Bem-vindo ao Elite" - Barlow Condensed 30/32, 700, uppercase, ls 0.03em
                        Text(
                            text = "BEM-VINDO AO ELITE",
                            fontFamily = EliteDisplayFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            lineHeight = 32.sp,
                            letterSpacing = 0.9.sp, // 0.03em * 30 = 0.9
                            color = TextColor,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Subtítulo muted: Inter 14/19.6
                        Text(
                            text = "Um plano de caminhada e corrida que respeita o seu fôlego, do primeiro passo até os 5 km.",
                            fontFamily = EliteBodyFont,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 19.6.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(18.dp))

                        // 3 Benefit cards (card.rw) - 40x40 icon + title + subtitle
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            BenefitCard(
                                icon = Icons.Filled.Shield,
                                title = "Tudo no seu celular",
                                subtitle = "Sem login e sem nuvem."
                            )
                            BenefitCard(
                                icon = Icons.Filled.TrendingUp,
                                title = "Progressivo",
                                subtitle = "A corrida aumenta aos poucos."
                            )
                            BenefitCard(
                                icon = Icons.Filled.AccessTime,
                                title = "Sem pressa",
                                subtitle = "Você escolhe os dias e a duração."
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))

                        // Campo de nome
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "COMO DEVEMOS TE CHAMAR?",
                                fontFamily = EliteBodyFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                letterSpacing = 1.32.sp, // 0.12em
                                color = TextMuted
                            )
                            OutlinedTextField(
                                value = userName,
                                onValueChange = onUserNameChange,
                                label = { Text("Seu nome ou apelido", fontFamily = EliteBodyFont, fontSize = 14.sp, color = TextMuted) },
                                placeholder = { Text("Seu nome ou apelido", fontFamily = EliteBodyFont, fontSize = 14.sp, color = TextMuted) },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Olive,
                                    unfocusedBorderColor = Olive,
                                    focusedTextColor = TextColor,
                                    unfocusedTextColor = TextColor,
                                    focusedLabelColor = Olive,
                                    unfocusedLabelColor = TextMuted,
                                    cursorColor = Olive
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("input_user_name"),
                                isError = showNameError
                            )
                            if (showNameError) {
                                Text(
                                    text = "O nome é obrigatório para continuar",
                                    fontFamily = EliteBodyFont,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = Red
                                )
                            }
                        }
                    }
                }
            }

            // CTA fixo na base: botão primário vermelho 56dp altura, raio 28
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                androidx.compose.material3.Button(
                    onClick = {
                        if (isNameValid) {
                            showNameError = false
                            onStartClick()
                        } else {
                            showNameError = true
                        }
                    },
                    enabled = isNameValid,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (isNameValid) Red else Red.copy(alpha = 0.4f),
                        contentColor = if (isNameValid) OnRed else OnRed.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("btn_comecar")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Começar",
                            fontFamily = EliteBodyFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            letterSpacing = 0.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.navigationBarsPadding().imePadding())
        }
    }
}

@Composable
private fun BenefitCard(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    // card.rw: 359x70, padding 14, gap 12, radius 18, border line (Line 10%), bg Card
    // Title: Barlow Condensed 700, uppercase, 14sp (matching spec t2)
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = CardColor,
            contentColor = TextColor
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Line), // Line = 10% osso
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container: 40x40, OliveSoft bg, Olive icon (22x22), radius 12
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(OliveSoft)
                    .padding(0.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Olive,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title.uppercase(), // spec has uppercase
                    fontFamily = EliteDisplayFont, // Barlow Condensed
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    color = TextColor,
                    letterSpacing = 0.56.sp // 0.04em * 14sp = 0.56sp
                )
                Text(
                    text = subtitle,
                    fontFamily = EliteBodyFont, // Inter
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 16.8.sp,
                    color = TextMuted
                )
            }
        }
    }
}