package com.example.mystudyapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.mystudyapp.ui.theme.mode_dark_primary
import com.example.mystudyapp.ui.theme.mode_dark_secondary
import com.example.mystudyapp.ui.theme.mode_dark_tertiary
import com.example.mystudyapp.ui.theme.mode_light_primary
import com.example.mystudyapp.ui.theme.mode_light_secondary
import com.example.mystudyapp.ui.theme.mode_light_tertiary

private val DarkColorScheme = darkColorScheme(
    primary = mode_dark_primary,
    secondary = mode_dark_secondary,
    tertiary = mode_dark_tertiary
)

private val LightColorScheme = lightColorScheme(
    primary = mode_light_primary,
    secondary = mode_light_secondary,
    tertiary = mode_light_tertiary
)

@Composable
fun MyStudyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb() // Or your desired status bar color
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme // Adjust based on status bar color
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}