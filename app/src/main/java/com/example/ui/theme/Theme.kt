package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.Black,
    primaryContainer = SafaaGreenDark,
    onPrimaryContainer = Color.White,
    secondary = DarkSecondary,
    onSecondary = Color.Black,
    secondaryContainer = AutoAmberDark,
    onSecondaryContainer = Color.White,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = Color(0xFFE2E6E2),
    onSurface = Color(0xFFE2E6E2),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SafaaGreen,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD4EEDF),
    onPrimaryContainer = SafaaGreenDark,
    secondary = AutoAmber,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = AutoAmberDark,
    tertiary = NepalGold,
    background = WarmWhite,
    surface = WarmSurface,
    surfaceVariant = WarmSurfaceVariant,
    onBackground = SlateDark,
    onSurface = SlateDark,
    onSurfaceVariant = SlateMedium,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
