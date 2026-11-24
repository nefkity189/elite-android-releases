package com.elite.sch.app.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Font families - these will be loaded from res/font/
val BarlowCondensedFontFamily = FontFamily(
    // Fonts will be added via Font() in the app's Theme.kt
)

val InterFontFamily = FontFamily(
    // Fonts will be added via Font() in the app's Theme.kt
)

// Text styles
val TimerTextStyle = TextStyle(
    fontFamily = BarlowCondensedFontFamily,
    fontWeight = FontWeight(600),
    fontSize = 32.sp,
    lineHeight = 32.sp,
    letterSpacing = 0.sp,
    // tabular numbers would be set via fontFeatureSettings
)

val DistancePaceValueStyle = TextStyle(
    fontFamily = BarlowCondensedFontFamily,
    fontWeight = FontWeight(600),
    fontSize = 40.sp,
    lineHeight = 40.sp,
    letterSpacing = 0.sp,
)

val DistancePaceUnitStyle = TextStyle(
    fontFamily = BarlowCondensedFontFamily,
    fontWeight = FontWeight(600),
    fontSize = 20.sp,
    lineHeight = 24.sp,
    letterSpacing = 0.sp,
)

val DistancePaceLabelStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight(600),
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 1.32.sp,
)

val RunWalkTitleStyle = TextStyle(
    fontFamily = BarlowCondensedFontFamily,
    fontWeight = FontWeight(600),
    fontSize = 20.sp,
    lineHeight = 23.sp,
    letterSpacing = 0.8.sp,
)

val StepLabelStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight(400),
    fontSize = 14.sp,
    lineHeight = 19.6.sp,
    letterSpacing = 0.sp,
)

val FreeRunLabelStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight(600),
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 1.32.sp,
)

val CameraPillStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight(400),
    fontSize = 13.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.sp,
)

val NextCardStyle = TextStyle(
    fontFamily = InterFontFamily,
    fontWeight = FontWeight(400),
    fontSize = 12.5.sp,
    lineHeight = 15.sp,
    letterSpacing = 0.sp,
)