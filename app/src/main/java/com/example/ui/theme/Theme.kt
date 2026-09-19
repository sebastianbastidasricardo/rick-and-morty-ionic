package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val RickAndMortyColorScheme =
  darkColorScheme(
    primary = PortalGreen,
    onPrimary = androidx.compose.ui.graphics.Color(0xFF003915),
    primaryContainer = androidx.compose.ui.graphics.Color(0xFF005322),
    onPrimaryContainer = PortalGreenLight,
    secondary = PortalCyan,
    onSecondary = androidx.compose.ui.graphics.Color(0xFF00363D),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFF004F58),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF80F2FF),
    background = SpaceBackground,
    onBackground = TextPrimary,
    surface = SpaceSurface,
    onSurface = TextPrimary,
    surfaceVariant = SpaceSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = SpaceCardBorder
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = RickAndMortyColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
