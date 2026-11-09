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
import androidx.compose.runtime.mutableIntStateOf
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
            if (sobreTiViewModel.bienvenidaCompleta) Destino.FEED else Destino.BIENVENIDA,
        )
    }
    var postId by rememberSaveable { mutableIntStateOf(-1) }
    var eventId by rememberSaveable { mutableIntStateOf(-1) }
    var detailOrigin by rememberSaveable { mutableStateOf(Destino.FEED.name) }
    LaunchedEffect(sobreTiViewModel.bienvenidaCompleta) {
        if (!sobreTiViewModel.bienvenidaCompleta) {
            destino = Destino.BIENVENIDA
        } else if (destino == Destino.BIENVENIDA) {
            destino = Destino.FEED
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
                        destino = Destino.FEED
                    },
                )
                Destino.FEED -> MainFeed(
                    onPostClick = { id ->
                        detailOrigin = Destino.FEED.name
                        postId = id
                        destino = Destino.POST_DETAIL
                    },
                    onEventClick = { id ->
                        detailOrigin = Destino.FEED.name
                        eventId = id
                        destino = Destino.EVENT_DETAIL
                    },
                )
                Destino.LIKED -> LikesScreen(
                    onPostClick = { id ->
                        detailOrigin = Destino.LIKED.name
                        postId = id
                        destino = Destino.POST_DETAIL
                    },
                    onEventClick = { id ->
                        detailOrigin = Destino.LIKED.name
                        eventId = id
                        destino = Destino.EVENT_DETAIL
                    },
                )
                Destino.POST_DETAIL -> PostDetailScreen(
                    postId = postId,
                    onBack = { destino = Destino.valueOf(detailOrigin) },
                )
                Destino.EVENT_DETAIL -> EventDetailScreen(
                    eventId = eventId,
                    onBack = { destino = Destino.valueOf(detailOrigin) },
                )
                Destino.PREGUNTAS -> Faq()
                Destino.AJUSTES -> Ajustes(onSobreTi = { destino = Destino.SOBRE_TI })
                Destino.SOBRE_TI -> SobreTi(onBack = { destino = Destino.AJUSTES })
            }
        }
        if (destino.muestraBarra) {
            BarraInferior(
                destino = destino,
                onFeed = { destino = Destino.FEED },
                onLiked = { destino = Destino.LIKED },
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
    if (destino == Destino.BIENVENIDA ||
        destino == Destino.POST_DETAIL ||
        destino == Destino.EVENT_DETAIL
    ) {
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
