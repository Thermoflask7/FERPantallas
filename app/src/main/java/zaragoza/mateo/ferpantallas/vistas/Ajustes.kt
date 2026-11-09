package zaragoza.mateo.ferpantallas.vistas

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import zaragoza.mateo.ferpantallas.modelos.TamanoTexto
import zaragoza.mateo.ferpantallas.modelos.TemaApp
import zaragoza.mateo.ferpantallas.ui.theme.FERPantallasTheme
import zaragoza.mateo.ferpantallas.viewmodels.AjustesViewModel
import zaragoza.mateo.ferpantallas.viewmodels.SobreTiViewModel

@Composable
fun Ajustes(
    onSobreTi: () -> Unit,
    viewModel: AjustesViewModel = viewModel(),
    sobreTiViewModel: SobreTiViewModel = viewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var avisosActivos by remember { mutableStateOf(notificacionesActivas(context)) }
    var confirmarBorrado by remember { mutableStateOf(false) }
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
        tamanoTexto = viewModel.tamanoTexto,
        onTamanoTexto = viewModel::onTamanoTextoSeleccionado,
        onSobreTi = onSobreTi,
        onNotificaciones = {
            if (avisosActivos || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                abrirAjustesNotificaciones(context)
            } else {
                pedirPermiso.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        },
        onBorrarDatos = {
            viewModel.borrarDatos()
            sobreTiViewModel.restablecer()
            confirmarBorrado = false
        },
        confirmarBorrado = confirmarBorrado,
        onPedirBorrado = { confirmarBorrado = true },
        onCancelarBorrado = { confirmarBorrado = false },
        version = versionApp(context),
    )
}

@Composable
fun AjustesContent(
    avisosActivos: Boolean,
    tema: TemaApp,
    onTema: (TemaApp) -> Unit,
    tamanoTexto: TamanoTexto,
    onTamanoTexto: (TamanoTexto) -> Unit,
    onSobreTi: () -> Unit,
    onNotificaciones: () -> Unit,
    onBorrarDatos: () -> Unit,
    confirmarBorrado: Boolean,
    onPedirBorrado: () -> Unit,
    onCancelarBorrado: () -> Unit,
    version: String,
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
        SeccionRadios(
            titulo = stringResource(R.string.ajustes_apariencia),
            opciones = TemaApp.entries.map { opcion ->
                OpcionAjuste(
                    texto = textoTema(opcion),
                    seleccionada = tema == opcion,
                    onClick = { onTema(opcion) },
                )
            },
        )
        SeccionRadios(
            titulo = stringResource(R.string.ajustes_accesibilidad),
            subtitulo = stringResource(R.string.ajustes_tamano_texto),
            opciones = TamanoTexto.entries.map { opcion ->
                OpcionAjuste(
                    texto = textoTamano(opcion),
                    seleccionada = tamanoTexto == opcion,
                    onClick = { onTamanoTexto(opcion) },
                )
            },
        )
        FilaAjustes(
            titulo = stringResource(R.string.ajustes_borrar),
            onClick = onPedirBorrado,
            conFlecha = false,
            colorTitulo = MaterialTheme.colorScheme.error,
        )
        TarjetaAcercaDe(version = version)
        if (confirmarBorrado) {
            AlertDialog(
                onDismissRequest = onCancelarBorrado,
                title = { Text(text = stringResource(R.string.ajustes_borrar_titulo)) },
                text = { Text(text = stringResource(R.string.ajustes_borrar_mensaje)) },
                confirmButton = {
                    TextButton(onClick = onBorrarDatos) {
                        Text(
                            text = stringResource(R.string.ajustes_borrar_confirmar),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = onCancelarBorrado) {
                        Text(text = stringResource(R.string.ajustes_cancelar))
                    }
                },
            )
        }
    }
}

private data class OpcionAjuste(
    val texto: String,
    val seleccionada: Boolean,
    val onClick: () -> Unit,
)

@Composable
private fun SeccionRadios(
    titulo: String,
    opciones: List<OpcionAjuste>,
    subtitulo: String? = null,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
            Text(
                text = titulo,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
            if (subtitulo != null) {
                Text(
                    text = subtitulo,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            opciones.forEach { opcion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = opcion.onClick)
                        .padding(end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = opcion.seleccionada,
                        onClick = opcion.onClick,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorResource(R.color.azulfer),
                        ),
                    )
                    Text(
                        text = opcion.texto,
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
private fun textoTamano(tamano: TamanoTexto): String {
    return when (tamano) {
        TamanoTexto.NORMAL -> stringResource(R.string.tamano_normal)
        TamanoTexto.GRANDE -> stringResource(R.string.tamano_grande)
        TamanoTexto.MUY_GRANDE -> stringResource(R.string.tamano_muy_grande)
    }
}

@Composable
private fun FilaAjustes(
    titulo: String,
    onClick: () -> Unit,
    subtitulo: String? = null,
    conFlecha: Boolean = true,
    colorTitulo: Color = Color.Unspecified,
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
                    color = colorTitulo,
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
            if (conFlecha) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colorResource(R.color.azulfer),
                )
            }
        }
    }
}

@Composable
private fun TarjetaAcercaDe(version: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = stringResource(R.string.ajustes_acerca),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = stringResource(R.string.acerca_fundacion),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(R.string.acerca_version, version),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(R.string.faq_contacto_celular),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(R.string.faq_contacto_correo),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = stringResource(R.string.acerca_privacidad),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

private fun versionApp(context: Context): String {
    val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.PackageInfoFlags.of(0),
        )
    } else {
        @Suppress("DEPRECATION")
        context.packageManager.getPackageInfo(context.packageName, 0)
    }
    return info.versionName.orEmpty()
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
            tamanoTexto = TamanoTexto.NORMAL,
            onTamanoTexto = {},
            onSobreTi = {},
            onNotificaciones = {},
            onBorrarDatos = {},
            confirmarBorrado = false,
            onPedirBorrado = {},
            onCancelarBorrado = {},
            version = "1.0",
        )
    }
}
