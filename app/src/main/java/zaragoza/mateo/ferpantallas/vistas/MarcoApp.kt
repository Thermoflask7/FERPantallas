package zaragoza.mateo.ferpantallas.vistas

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import zaragoza.mateo.ferpantallas.modelos.Destino
import zaragoza.mateo.ferpantallas.modelos.TemaApp
import zaragoza.mateo.ferpantallas.viewmodels.AjustesViewModel
import zaragoza.mateo.ferpantallas.viewmodels.SobreTiViewModel

@Composable
fun MarcoApp(
    ajustesViewModel: AjustesViewModel = viewModel(),
    sobreTiViewModel: SobreTiViewModel = viewModel(),
) {
    var destino by rememberSaveable(
        stateSaver = Saver(
            save = { it.name },
            restore = { Destino.valueOf(it) },
        ),
    ) {
        mutableStateOf(
            if (sobreTiViewModel.bienvenidaCompleta) Destino.PREGUNTAS else Destino.BIENVENIDA,
        )
    }
    LaunchedEffect(sobreTiViewModel.bienvenidaCompleta) {
        if (!sobreTiViewModel.bienvenidaCompleta) {
            destino = Destino.BIENVENIDA
        } else if (destino == Destino.BIENVENIDA) {
            destino = Destino.PREGUNTAS
        }
    }
    IconosDeSistema(destino = destino, tema = ajustesViewModel.tema)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .then(insetsDe(destino)),
    ) {
        if (destino.muestraEncabezado) {
            EncabezadoFer()
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            when (destino) {
                Destino.BIENVENIDA -> Bienvenida(
                    onContinuar = { perfil, nivel ->
                        sobreTiViewModel.guardarBienvenida(perfil, nivel)
                        destino = Destino.PREGUNTAS
                    },
                )
                Destino.PREGUNTAS -> Faq()
                Destino.AJUSTES -> Ajustes(onSobreTi = { destino = Destino.SOBRE_TI })
                Destino.SOBRE_TI -> SobreTi(onBack = { destino = Destino.AJUSTES })
            }
        }
        if (destino.muestraBarra) {
            BarraInferior(
                destino = destino,
                onPreguntas = { destino = Destino.PREGUNTAS },
                onAjustes = { destino = Destino.AJUSTES },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
private fun IconosDeSistema(destino: Destino, tema: TemaApp) {
    val oscuro = when (tema) {
        TemaApp.CLARO -> false
        TemaApp.OSCURO -> true
        TemaApp.SISTEMA -> isSystemInDarkTheme()
    }
    val view = LocalView.current
    if (view.isInEditMode) {
        return
    }
    val activity = view.context.buscarActividad() ?: return
    SideEffect {
        val iconosOscuros = destino != Destino.BIENVENIDA && !oscuro
        val controller = WindowCompat.getInsetsController(activity.window, view)
        controller.isAppearanceLightStatusBars = iconosOscuros
        controller.isAppearanceLightNavigationBars = iconosOscuros
    }
}

@Composable
private fun insetsDe(destino: Destino): Modifier {
    if (destino == Destino.BIENVENIDA) {
        return Modifier
    }
    return Modifier
        .windowInsetsPadding(WindowInsets.safeDrawing)
        .imePadding()
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
