package zaragoza.mateo.ferpantallas.vistas

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import zaragoza.mateo.ferpantallas.R
import zaragoza.mateo.ferpantallas.modelos.TemaApp
import zaragoza.mateo.ferpantallas.ui.theme.FERPantallasTheme
import zaragoza.mateo.ferpantallas.viewmodels.AjustesViewModel

@Composable
fun Ajustes(
    onSobreTi: () -> Unit,
    viewModel: AjustesViewModel = viewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var avisosActivos by remember { mutableStateOf(notificacionesActivas(context)) }
    val pedirPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { concedido ->
        avisosActivos = concedido || notificacionesActivas(context)
        if (!concedido && !puedeVolverAPedirPermiso(context)) {
            abrirAjustesNotificaciones(context)
        }
    }
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                avisosActivos = notificacionesActivas(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    AjustesContent(
        avisosActivos = avisosActivos,
        tema = viewModel.tema,
        onTema = viewModel::onTemaSeleccionado,
        onSobreTi = onSobreTi,
        onNotificaciones = {
            if (avisosActivos || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                abrirAjustesNotificaciones(context)
            } else {
                pedirPermiso.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        },
    )
}

@Composable
fun AjustesContent(
    avisosActivos: Boolean,
    tema: TemaApp,
    onTema: (TemaApp) -> Unit,
    onSobreTi: () -> Unit,
    onNotificaciones: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.ajustes_titulo),
            style = MaterialTheme.typography.titleLarge,
        )
        FilaAjustes(
            titulo = stringResource(R.string.sobre_ti_titulo),
            onClick = onSobreTi,
        )
        FilaAjustes(
            titulo = stringResource(R.string.ajustes_notificaciones),
            subtitulo = stringResource(
                if (avisosActivos) {
                    R.string.ajustes_notificaciones_activas
                } else {
                    R.string.ajustes_notificaciones_apagadas
                },
            ),
            onClick = onNotificaciones,
        )
        SeccionApariencia(tema = tema, onTema = onTema)
    }
}

@Composable
private fun SeccionApariencia(
    tema: TemaApp,
    onTema: (TemaApp) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
            Text(
                text = stringResource(R.string.ajustes_apariencia),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
            TemaApp.entries.forEach { opcion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTema(opcion) }
                        .padding(end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = tema == opcion,
                        onClick = { onTema(opcion) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorResource(R.color.azulfer),
                        ),
                    )
                    Text(
                        text = textoTema(opcion),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun textoTema(tema: TemaApp): String {
    return when (tema) {
        TemaApp.CLARO -> stringResource(R.string.tema_claro)
        TemaApp.OSCURO -> stringResource(R.string.tema_oscuro)
        TemaApp.SISTEMA -> stringResource(R.string.tema_sistema)
    }
}

@Composable
private fun FilaAjustes(
    titulo: String,
    onClick: () -> Unit,
    subtitulo: String? = null,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.bodyLarge,
                )
                if (subtitulo != null) {
                    Text(
                        text = subtitulo,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colorResource(R.color.azulfer),
            )
        }
    }
}

private fun notificacionesActivas(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}

private fun puedeVolverAPedirPermiso(context: Context): Boolean {
    val activity = context.buscarActividad() ?: return false
    return activity.shouldShowRequestPermissionRationale(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )
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

private fun abrirAjustesNotificaciones(context: Context) {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
    } else {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun AjustesPreview() {
    FERPantallasTheme {
        AjustesContent(
            avisosActivos = true,
            tema = TemaApp.SISTEMA,
            onTema = {},
            onSobreTi = {},
            onNotificaciones = {},
        )
    }
}
