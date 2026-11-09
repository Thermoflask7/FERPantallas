package zaragoza.mateo.ferpantallas.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zaragoza.mateo.ferpantallas.modelos.Destino

@Composable
fun MarcoApp() {
    var destino by rememberSaveable(
        stateSaver = Saver(
            save = { it.name },
            restore = { Destino.valueOf(it) },
        ),
    ) {
        mutableStateOf(Destino.PREGUNTAS)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding(),
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
