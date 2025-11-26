package com.chefkiss.app.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🎨 Paleta de colores
val CoralPrimary = Color(0xFFFF6F61)
val AccentLight = Color(0xFFFFE5E0)
val AccentBorder = Color(0xFFFFC4B3)
val TextPrimary = Color(0xFF1C1C1E)
val TextSecondary = Color(0xFF6E6E73)
val StarYellow = Color(0xFFFFD700)
val SurfaceVariant = Color(0xFFF5F5F7)
val Background = Color(0xFFFFFFFF)
val GlassWhite = Color(0x80FFFFFF)

// 🌓 Temas claros y oscuros
private val LightColors = lightColorScheme(
    primary = CoralPrimary,
    background = Background,
    surface = Background,
    onPrimary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

private val DarkColors = darkColorScheme(
    primary = CoralPrimary,
    background = Color(0xFF1C1C1E),
    surface = Color(0xFF2C2C2E),
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// 🌗 Tema general
@Composable
fun ChefKissTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content
    )
}