package com.elite.sch.app.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elite.sch.app.ui.theme.EliteBorder
import com.elite.sch.app.ui.theme.EliteCard
import com.elite.sch.app.ui.theme.EliteError
import com.elite.sch.app.ui.theme.EliteFieldBorder
import com.elite.sch.app.ui.theme.EliteMotion
import com.elite.sch.app.ui.theme.EliteOnPrimary
import com.elite.sch.app.ui.theme.ElitePrimary
import com.elite.sch.app.ui.theme.EliteShape
import com.elite.sch.app.ui.theme.EliteSpacing
import com.elite.sch.app.ui.theme.EliteTextPrimary
import com.elite.sch.app.ui.theme.EliteTextSecondary
import com.elite.sch.app.ui.theme.EliteTrack
import com.elite.sch.app.ui.theme.EliteTurquoise
import com.elite.sch.app.ui.theme.EliteWarning
import com.elite.sch.app.ui.theme.Field
import com.elite.sch.app.ui.theme.RedLine
import com.elite.sch.app.ui.theme.RedSoft
import com.elite.sch.app.ui.theme.RunnaGlassBorder
import com.elite.sch.app.ui.theme.RunnaNumberFont
import com.elite.sch.app.ui.theme.RunnaNumberStyles
import com.elite.sch.app.ui.theme.RunnaRedDot
import com.elite.sch.app.ui.theme.RunnaTeal
import com.elite.sch.app.ui.theme.RunnaTealOn
import com.elite.sch.app.ui.theme.RunnaTextFont
import com.elite.sch.app.ui.theme.RunnaTextSecondary
import com.elite.sch.app.ui.theme.RunnaTextStyles
import com.elite.sch.app.ui.components.RunnaTheme
import com.elite.sch.app.ui.theme.TealSoft
import com.elite.sch.app.ui.theme.TrackOffBorder
import com.elite.sch.app.ui.components.eliteTextFieldColors
import com.elite.sch.app.ui.theme.animateFloatAsStateWithReduceMotion
import com.elite.sch.app.ui.theme.rememberReducedMotion
import com.elite.sch.app.R
import com.elite.sch.app.ui.theme.Card as CardColor
import com.elite.sch.app.ui.theme.EliteBodyFont
import com.elite.sch.app.ui.theme.Line
import com.elite.sch.app.ui.theme.LineStrong
import com.elite.sch.app.ui.theme.Olive
import com.elite.sch.app.ui.theme.OliveDark
import com.elite.sch.app.ui.theme.OliveSoft
import com.elite.sch.app.ui.theme.OnOlive
import com.elite.sch.app.ui.theme.Text as TextColor
import com.elite.sch.app.ui.theme.TextMuted
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHeader(
    userName: String,
    onEditClick: () -> Unit,
    isEditing: Boolean,
    modifier: Modifier = Modifier
) {
    val displayName = userName.ifBlank { "Recruta" }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(94.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, Line, RoundedCornerShape(18.dp))
            .background(CardColor)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(Color(0xFF262B1A), Color(0xFF0A0B09))
                    )
                )
                .border(2.dp, OliveDark, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.drawable.ic_elite_mark),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = displayName,
                fontFamily = RunnaNumberFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                lineHeight = 23.sp,
                letterSpacing = 0.8.sp,
                color = TextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "MISSÃO DADA É MISSÃO CUMPRIDA",
                fontFamily = EliteBodyFont,
                fontWeight = FontWeight.SemiBold,
                fontSize = 8.sp,
                lineHeight = 10.sp,
                letterSpacing = 1.04.sp,
                color = TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .size(44.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .border(1.dp, if (isEditing) Olive else Line, androidx.compose.foundation.shape.CircleShape)
                .testTag("btn_edit_name")
                .semantics { contentDescription = "Editar nome" }
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                tint = if (isEditing) Olive else TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun NameEditor(
    nameInput: String,
    onNameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    nameSavedMessage: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Como devemos te chamar?",
            style = RunnaTextStyles.LabelSmall.copy(fontWeight = FontWeight.Medium),
            color = RunnaTextSecondary
        )

        OutlinedTextField(
            value = nameInput,
            onValueChange = onNameChange,
            placeholder = { Text("Seu nome ou apelido", color = RunnaTextSecondary) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = eliteTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .testTag("profile_name_input"),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = RunnaTextFont,
                fontSize = 16.sp,
                lineHeight = 22.sp
            ),
            label = { Text("Como devemos te chamar?", color = RunnaTextSecondary) }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (nameSavedMessage) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        tint = Olive,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Nome salvo!",
                        style = RunnaTextStyles.Label.copy(fontWeight = FontWeight.Medium),
                        color = Olive
                    )
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Save button
            Button(
                onClick = onSaveClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Olive,
                    contentColor = OnOlive
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .height(48.dp)
                    .padding(horizontal = 20.dp)
                    .testTag("profile_save_name_button"),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp)
            ) {
                Text(
                    text = "Salvar",
                    style = RunnaTextStyles.Button
                )
            }
        }
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(Locale.ROOT),
        style = com.elite.sch.app.ui.theme.EliteTextStyles.Label,
        color = TextMuted,
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun SettingsGroup(
    modifier: Modifier = Modifier,
    danger: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val borderColor = if (danger) RedLine else Line

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .background(CardColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }
}

@Composable
fun Hairline(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(Line)
    )
}

@Composable
fun SwitchRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    testTag: String? = null,
    onDescriptionClick: (() -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val reducedMotion = rememberReducedMotion()
    val scale by animateFloatAsStateWithReduceMotion(
        targetValue = if (pressed) 0.97f else 1f,
        reduceMotion = reducedMotion,
        durationMillis = EliteMotion.ScalePressDuration,
        label = "switchPressScale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 36.dp)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            )
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(),
                enabled = enabled,
                onClickLabel = title
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                onCheckedChange(!checked)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title + description
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = RunnaTextFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 17.sp
                ),
                color = if (enabled) TextColor else TextMuted
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = RunnaTextFont,
                        fontSize = 12.sp,
                        lineHeight = 16.8.sp
                    ),
                    color = TextMuted,
                    modifier = if (onDescriptionClick != null) {
                        Modifier.clickable(onClick = onDescriptionClick)
                    } else {
                        Modifier
                    }
                )
            }
        }

        // Switch - use Material 3 Switch with correct parameters
        Switch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextColor,
                checkedTrackColor = OliveDark,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = CardColor,
                uncheckedBorderColor = LineStrong,
                disabledCheckedThumbColor = TextColor.copy(alpha = 0.5f),
                disabledCheckedTrackColor = OliveDark.copy(alpha = 0.5f),
                disabledUncheckedThumbColor = TextMuted.copy(alpha = 0.5f),
                disabledUncheckedTrackColor = CardColor.copy(alpha = 0.5f),
                disabledUncheckedBorderColor = LineStrong.copy(alpha = 0.5f)
            ),
            modifier = if (testTag != null) Modifier.testTag(testTag) else Modifier
        )
    }
}

@Composable
fun SegmentedControl(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    onOptionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    testTags: List<String>? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (label.isNotBlank()) {
            Text(
                text = label,
                style = RunnaTextStyles.Label.copy(fontWeight = FontWeight.Medium),
                color = TextMuted
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CardColor)
                .then(if (label.isNotBlank()) Modifier.border(1.dp, Line, RoundedCornerShape(12.dp)) else Modifier),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp))
                        .then(
                            if (isSelected) Modifier.background(Olive)
                            else Modifier
                                .border(1.dp, Line, RoundedCornerShape(14.dp))
                                .background(CardColor)
                        )
                        .selectable(
                            selected = isSelected,
                            role = Role.RadioButton,
                            onClick = { onOptionClick(index) }
                        )
                        .then(if (testTags != null && index < testTags.size) Modifier.testTag(testTags[index]) else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = RunnaTextFont,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            fontSize = 12.sp,
                            lineHeight = 20.sp
                        ),
                        color = if (isSelected) OnOlive else TextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun RadioList(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    onOptionClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    testTags: List<String>? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 54.dp)
            .padding(top = 14.dp, bottom = 6.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Text(
            text = label,
            style = RunnaTextStyles.Label.copy(fontWeight = FontWeight.Medium),
            color = RunnaTextSecondary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        options.forEachIndexed { index, option ->
            val isSelected = index == selectedIndex
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(end = 16.dp)
                    .selectable(
                        selected = isSelected,
                        role = Role.RadioButton,
                        onClick = { onOptionClick(index) }
                    )
                    .then(if (index > 0) Modifier.border(1.dp, RunnaGlassBorder, RoundedCornerShape(0.dp)) else Modifier)
                    .then(if (testTags != null && index < testTags.size) Modifier.testTag(testTags[index]) else Modifier),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = RunnaTextFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    color = EliteTextPrimary
                )

                // Radio circle
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .border(2.dp, if (isSelected) RunnaTeal else TrackOffBorder, androidx.compose.foundation.shape.CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(RunnaTeal)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NavRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String? = null
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val reducedMotion = rememberReducedMotion()
    val scale by animateFloatAsStateWithReduceMotion(
        targetValue = if (pressed) 0.97f else 1f,
        reduceMotion = reducedMotion,
        durationMillis = EliteMotion.ScalePressDuration,
        label = "navPressScale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 66.dp)
            .padding(horizontal = 14.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(),
                onClickLabel = title
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = RunnaTextFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 17.sp
                ),
                color = TextColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = RunnaTextFont,
                    fontSize = 12.sp,
                    lineHeight = 16.8.sp
                ),
                color = TextMuted
            )
        }

        Icon(
            imageVector = Icons.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ValueRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String? = null
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val reducedMotion = rememberReducedMotion()
    val scale by animateFloatAsStateWithReduceMotion(
        targetValue = if (pressed) 0.97f else 1f,
        reduceMotion = reducedMotion,
        durationMillis = EliteMotion.ScalePressDuration,
        label = "valuePressScale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 14.dp, horizontal = 16.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(),
                onClickLabel = title
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RunnaTextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = RunnaTextFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                ),
                color = EliteTextPrimary
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = RunnaNumberStyles.MetricLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    lineHeight = 26.sp
                ),
                color = RunnaTeal
            )
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = RunnaTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SettingsNotice(
    message: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0x55E0A93A), RoundedCornerShape(12.dp))
            .background(Color(0x1FE0A93A)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = null,
                tint = com.elite.sch.app.ui.theme.Amber,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = RunnaTextFont,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.2.sp
                ),
                color = TextColor,
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Olive
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Line),
            modifier = Modifier
                .width(220.dp)
                .height(40.dp)
                .padding(top = 10.dp)
                .testTag(testTag ?: "btn_open_notification_settings"),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
        ) {
            Text(
                text = buttonText,
                style = RunnaTextStyles.Label.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    testTag: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RunnaTextSecondary,
            modifier = Modifier
                .size(22.dp)
                .padding(top = 1.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = RunnaTextFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                ),
                color = EliteTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = RunnaTextFont,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    letterSpacing = 0.2.sp
                ),
                color = RunnaTextSecondary
            )
        }
    }
}

@Composable
fun PrivacyInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RunnaTeal,
            modifier = Modifier
                .size(22.dp)
                .padding(top = 1.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = RunnaTextFont,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.2.sp
            ),
            color = RunnaTextSecondary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ActionRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String? = null,
    destructive: Boolean = false
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val reducedMotion = rememberReducedMotion()
    val scale by animateFloatAsStateWithReduceMotion(
        targetValue = if (pressed) 0.97f else 1f,
        reduceMotion = reducedMotion,
        durationMillis = EliteMotion.ScalePressDuration,
        label = "actionPressScale"
    )

    val textColor = if (destructive) RunnaRedDot else EliteTextPrimary
    val iconColor = if (destructive) RunnaRedDot else RunnaTextSecondary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 14.dp, horizontal = 16.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(),
                onClickLabel = title
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = RunnaTextFont,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 20.sp
            ),
            color = textColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Footnote(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(
            fontFamily = RunnaTextFont,
            fontSize = 12.5.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.2.sp
        ),
        color = RunnaTextSecondary,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp, top = 12.dp)
    )
}