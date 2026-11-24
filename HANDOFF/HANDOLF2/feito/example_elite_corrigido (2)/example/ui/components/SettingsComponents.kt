package com.example.ui.components

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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AisleBorder
import com.example.ui.theme.AisleCard
import com.example.ui.theme.AisleError
import com.example.ui.theme.AisleFieldBorder
import com.example.ui.theme.AisleMotion
import com.example.ui.theme.AisleOnPrimary
import com.example.ui.theme.AislePrimary
import com.example.ui.theme.AisleShape
import com.example.ui.theme.AisleSpacing
import com.example.ui.theme.AisleTextPrimary
import com.example.ui.theme.AisleTextSecondary
import com.example.ui.theme.AisleTrack
import com.example.ui.theme.AisleTurquoise
import com.example.ui.theme.AisleWarning
import com.example.ui.theme.Field
import com.example.ui.theme.RedLine
import com.example.ui.theme.RedSoft
import com.example.ui.theme.RunnaGlassBorder
import com.example.ui.theme.RunnaNumberFont
import com.example.ui.theme.RunnaNumberStyles
import com.example.ui.theme.RunnaRedDot
import com.example.ui.theme.RunnaTeal
import com.example.ui.theme.RunnaTealOn
import com.example.ui.theme.RunnaTextFont
import com.example.ui.theme.RunnaTextSecondary
import com.example.ui.theme.RunnaTextStyles
import com.example.ui.components.RunnaTheme
import com.example.ui.theme.TealSoft
import com.example.ui.theme.TrackOffBorder
import com.example.ui.components.aisleTextFieldColors
import com.example.ui.theme.animateFloatAsStateWithReduceMotion
import com.example.ui.theme.rememberReducedMotion
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHeader(
    userName: String,
    onEditClick: () -> Unit,
    isEditing: Boolean,
    modifier: Modifier = Modifier
) {
    val displayName = userName.ifBlank { "Corredor Iniciante" }
    val initial = displayName.firstOrNull()?.uppercaseChar() ?: 'C'

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(TealSoft),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial.toString(),
                style = RunnaNumberStyles.MetricLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp,
                    lineHeight = 34.sp
                ),
                color = RunnaTeal
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Name + subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = displayName,
                style = RunnaNumberStyles.MetricLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 28.sp,
                    lineHeight = 32.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Rumo aos seus 5K no Aisle",
                style = RunnaTextStyles.LabelSmall,
                color = RunnaTextSecondary
            )
        }

        // Edit button
        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, if (isEditing) RunnaTeal else RunnaGlassBorder, RoundedCornerShape(12.dp))
                .testTag("btn_edit_name")
                .semantics { contentDescription = "Editar nome" }
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                tint = if (isEditing) RunnaTeal else RunnaTextSecondary,
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
            colors = aisleTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
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
                        tint = RunnaTeal,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Nome salvo!",
                        style = RunnaTextStyles.Label.copy(fontWeight = FontWeight.Medium),
                        color = RunnaTeal
                    )
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Save button
            Button(
                onClick = onSaveClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RunnaTeal,
                    contentColor = RunnaTealOn
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
        text = text,
        style = RunnaNumberStyles.MetricLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 24.sp
            ),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 28.dp, bottom = 12.dp)
    )
}

@Composable
fun SettingsGroup(
    modifier: Modifier = Modifier,
    danger: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val borderColor = if (danger) RedLine else RunnaGlassBorder

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .background(AisleCard),
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
            .height(1.dp)
            .background(RunnaGlassBorder)
            .padding(start = 54.dp)
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
    testTag: String? = null
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val reducedMotion = rememberReducedMotion()
    val scale by animateFloatAsStateWithReduceMotion(
        targetValue = if (pressed) 0.97f else 1f,
        reduceMotion = reducedMotion,
        durationMillis = AisleMotion.ScalePressDuration,
        label = "switchPressScale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(vertical = 14.dp, horizontal = 16.dp)
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
        // Icon
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = RunnaTextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        // Title + description
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = RunnaTextFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                ),
                color = if (enabled) AisleTextPrimary else AisleTextSecondary
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = RunnaTextFont,
                        fontSize = 12.5.sp,
                        lineHeight = 17.sp,
                        letterSpacing = 0.2.sp
                    ),
                    color = RunnaTextSecondary
                )
            }
        }

        // Switch - use Material 3 Switch with correct parameters
        Switch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = RunnaTealOn,
                checkedTrackColor = RunnaTeal,
                uncheckedThumbColor = RunnaTextSecondary,
                uncheckedTrackColor = RunnaTheme.ItemOffBg,
                uncheckedBorderColor = TrackOffBorder,
                disabledCheckedThumbColor = RunnaTealOn.copy(alpha = 0.5f),
                disabledCheckedTrackColor = RunnaTeal.copy(alpha = 0.5f),
                disabledUncheckedThumbColor = RunnaTextSecondary.copy(alpha = 0.5f),
                disabledUncheckedTrackColor = RunnaTheme.ItemOffBg.copy(alpha = 0.5f),
                disabledUncheckedBorderColor = TrackOffBorder.copy(alpha = 0.5f)
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
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, start = 54.dp, end = 16.dp, top = 2.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = RunnaTextStyles.Label.copy(fontWeight = FontWeight.Medium),
            color = RunnaTextSecondary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, RunnaGlassBorder, RoundedCornerShape(12.dp))
                .background(Field)
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(if (index == 0) RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                        else if (index == options.lastIndex) RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                        else RoundedCornerShape(0.dp))
                        .then(if (index > 0) Modifier.border(1.dp, RunnaGlassBorder, RoundedCornerShape(0.dp)) else Modifier)
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
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        ),
                        color = if (isSelected) RunnaTeal else RunnaTextSecondary
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
                    color = AisleTextPrimary
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
        durationMillis = AisleMotion.ScalePressDuration,
        label = "navPressScale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(67.dp)
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
                color = AisleTextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = RunnaTextFont,
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp,
                    letterSpacing = 0.2.sp
                ),
                color = RunnaTextSecondary
            )
        }

        Icon(
            imageVector = Icons.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = RunnaTextSecondary,
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
        durationMillis = AisleMotion.ScalePressDuration,
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
                color = AisleTextPrimary
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
            .border(1.dp, RedLine, RoundedCornerShape(12.dp))
            .background(RedSoft),
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
                tint = RunnaRedDot,
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
                color = AisleTextPrimary,
                modifier = Modifier.weight(1f)
            )
        }

        Button(
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = RunnaTeal
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, RunnaGlassBorder),
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
                color = AisleTextPrimary
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
        durationMillis = AisleMotion.ScalePressDuration,
        label = "actionPressScale"
    )

    val textColor = if (destructive) RunnaRedDot else AisleTextPrimary
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