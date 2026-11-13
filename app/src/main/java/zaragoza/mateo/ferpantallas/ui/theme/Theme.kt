package zaragoza.mateo.ferpantallas.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat
import zaragoza.mateo.ferpantallas.R

@Composable
fun FERPantallasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    textScale: Float = 1f,
    content: @Composable () -> Unit,
) {
    val accent = colorResource(R.color.fer_blue)
    val background = colorResource(R.color.fer_background)
    val textGray = colorResource(R.color.fer_text_gray)
    val lightGray = colorResource(R.color.fer_light_gray)
    val brightBlue = colorResource(R.color.fer_bright_blue)

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = brightBlue,
            onPrimary = Color.White,
            secondary = lightGray,
            tertiary = lightGray,
        )
    } else {
        lightColorScheme(
            primary = accent,
            onPrimary = Color.White,
            secondary = textGray,
            onSecondary = Color.White,
            tertiary = lightGray,
            onTertiary = Color.White,
            background = background,
            onBackground = textGray,
            surface = Color.White,
            onSurface = textGray,
            onSurfaceVariant = textGray,
            surfaceVariant = Color.White,
            surfaceContainer = Color.White,
            surfaceContainerLow = Color.White,
            surfaceContainerHigh = Color.White,
            surfaceContainerHighest = Color.White,
        )
    }

    val view = LocalView.current
    val activity = view.context.findActivity()
    if (!view.isInEditMode && activity != null) {
        SideEffect {
            val controller = WindowCompat.getInsetsController(activity.window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        val density = LocalDensity.current
        CompositionLocalProvider(
            LocalContentColor provides colorScheme.onSurface,
            LocalDensity provides Density(
                density = density.density,
                fontScale = density.fontScale * textScale,
            ),
        ) {
            content()
        }
    }
}

private fun Context.findActivity(): Activity? {
    var current: Context = this
    while (current is ContextWrapper) {
        if (current is Activity) {
            return current
        }
        current = current.baseContext
    }
    return null
}