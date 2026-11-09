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
    escalaTexto: Float = 1f,
    content: @Composable () -> Unit,
) {
    val azulFer = colorResource(R.color.azulfer)
    val colorScheme = if (darkTheme) {
        darkColorScheme(primary = azulFer, onPrimary = Color.White)
    } else {
        lightColorScheme(primary = azulFer, onPrimary = Color.White)
    }
    val view = LocalView.current
    val activity = view.context.buscarActividad()
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
        val densidad = LocalDensity.current
        CompositionLocalProvider(
            LocalContentColor provides colorScheme.onSurface,
            LocalDensity provides Density(
                density = densidad.density,
                fontScale = densidad.fontScale * escalaTexto,
            ),
        ) {
            content()
        }
    }
}

private fun Context.buscarActividad(): Activity? {
    var actual: Context = this
    while (actual is ContextWrapper) {
        if (actual is Activity) {
            return actual
        }
        actual = actual.baseContext
    }
    return null
}
