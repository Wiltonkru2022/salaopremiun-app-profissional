package br.com.salaopremiun.profissional.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimaryDark,
    onPrimary = Ink,
    secondary = Success,
    tertiary = Warning,
    background = Ink,
    onBackground = Color(0xFFF8F7F2),
    surface = Charcoal,
    onSurface = Color(0xFFF8F7F2),
    surfaceVariant = Color(0xFF3A3A3A),
    onSurfaceVariant = Color(0xFFD7D3C8),
    outline = Color(0xFF5B5B5B),
)

private val LightColorScheme = lightColorScheme(
    primary = GoldPrimary,
    onPrimary = Color.White,
    secondary = Success,
    tertiary = Warning,
    background = Paper,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = SurfaceSoft,
    onSurfaceVariant = Color(0xFF667085),
    outline = Line,
)

@Composable
fun SalaoPremiunProfissionalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
