package com.yapp.designsystem.theme

import Ownglyph
import Pretendard
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class OrbitTypography(
    val displaySemiBold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 56.sp,
        lineHeight = 72.sp,
        letterSpacing = (-1.7864).sp,
    ),
    val displayBold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = (-1.3536).sp,
    ),
    val title1Bold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.972).sp,
    ),
    val title1Medium: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 36.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.972).sp,
    ),
    val title2Bold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.828).sp,
    ),
    val title2SemiBold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.828).sp,
    ),
    val title2Medium: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.828).sp,
    ),
    val title3SemiBold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.552).sp,
    ),
    val heading1SemiBold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.352).sp,
    ),
    val heading2SemiBold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.24).sp,
    ),
    val headline1SemiBold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.18).sp,
    ),
    val headline2SemiBold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.17).sp,
    ),
    val headline2Medium: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.17).sp,
    ),
    val body1Bold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.16).sp,
    ),
    val body1SemiBold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.16).sp,
    ),
    val body1Medium: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.16).sp,
    ),
    val body1Regular: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.16).sp,
    ),
    val body2Medium: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.15).sp,
    ),
    val body2Regular: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.15).sp,
    ),
    val label1SemiBold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.14).sp,
    ),
    val label1Medium: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.14).sp,
    ),
    val label2SemiBold: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.13).sp,
    ),
    val label2Regular: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.13).sp,
    ),
    val caption1Regular: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = (-0.12).sp,
    ),
    val caption2Regular: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = (-0.11).sp,
    ),
    val H0: TextStyle = TextStyle(
        fontFamily = Ownglyph,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 50.sp,
        letterSpacing = (-0.36).sp,
    ),
    val H1: TextStyle = TextStyle(
        fontFamily = Ownglyph,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 39.sp,
        letterSpacing = (-0.28).sp,
    ),
    val H2: TextStyle = TextStyle(
        fontFamily = Ownglyph,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 33.sp,
        letterSpacing = (-0.22).sp,
    ),
    val H3: TextStyle = TextStyle(
        fontFamily = Ownglyph,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.20).sp,
    ),
    val H4: TextStyle = TextStyle(
        fontFamily = Ownglyph,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 23.sp,
        letterSpacing = (-0.18).sp,
    ),
    val H4_150: TextStyle = TextStyle(
        fontFamily = Ownglyph,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 27.sp,
        letterSpacing = (-0.18).sp,
    ),
    val H5: TextStyle = TextStyle(
        fontFamily = Ownglyph,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 21.sp,
        letterSpacing = (-0.16).sp,
    ),
)

val LocalTypography = staticCompositionLocalOf { OrbitTypography() }
