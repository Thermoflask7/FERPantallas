package zaragoza.mateo.ferpantallas.vistas

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings as AndroidSettings
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
import androidx.compose.ui.graphics.luminance
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
import zaragoza.mateo.ferpantallas.modelos.TextSize
import zaragoza.mateo.ferpantallas.modelos.AppTheme
import zaragoza.mateo.ferpantallas.ui.theme.FERPantallasTheme
import zaragoza.mateo.ferpantallas.viewmodels.SettingsViewModel
import zaragoza.mateo.ferpantallas.viewmodels.EventVM
import zaragoza.mateo.ferpantallas.viewmodels.PostVM
import zaragoza.mateo.ferpantallas.viewmodels.AboutYouViewModel

@Composable
fun Settings(
    onAboutYou: () -> Unit,
    viewModel: SettingsViewModel = viewModel(),
    aboutYouViewModel: AboutYouViewModel = viewModel(),
    postViewModel: PostVM = viewModel(),
    eventViewModel: EventVM = viewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var notificationsOn by remember { mutableStateOf(notificationsEnabled(context)) }
    var confirmDeletion by remember { mutableStateOf(false) }
    val requestPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        notificationsOn = granted || notificationsEnabled(context)
        if (!granted && !canAskNotificationPermissionAgain(context)) {
            openNotificationSettings(context)
        }
    }
    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                notificationsOn = notificationsEnabled(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    SettingsContent(
        notificationsOn = notificationsOn,
        theme = viewModel.theme,
        onTheme = viewModel::onThemeSelected,
        textSize = viewModel.textSize,
        onTextSize = viewModel::onTextSizeSelected,
        onAboutYou = onAboutYou,
        onNotifications = {
            if (notificationsOn || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                openNotificationSettings(context)
            } else {
                requestPermission.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        },
        onClearData = {
            viewModel.clearData()
            aboutYouViewModel.reset()
            postViewModel.clearLocalState()
            eventViewModel.clearLocalState()
            confirmDeletion = false
        },
        confirmDeletion = confirmDeletion,
        onRequestDeletion = { confirmDeletion = true },
        onCancelDeletion = { confirmDeletion = false },
        version = appVersion(context),
    )
}

@Composable
fun SettingsContent(
    notificationsOn: Boolean,
    theme: AppTheme,
    onTheme: (AppTheme) -> Unit,
    textSize: TextSize,
    onTextSize: (TextSize) -> Unit,
    onAboutYou: () -> Unit,
    onNotifications: () -> Unit,
    onClearData: () -> Unit,
    confirmDeletion: Boolean,
    onRequestDeletion: () -> Unit,
    onCancelDeletion: () -> Unit,
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
        SettingsRow(
            title = stringResource(R.string.sobre_ti_titulo),
            onClick = onAboutYou,
        )
        SettingsRow(
            title = stringResource(R.string.ajustes_notificaciones),
            subtitle = stringResource(
                if (notificationsOn) {
                    R.string.ajustes_notificaciones_activas
                } else {
                    R.string.ajustes_notificaciones_apagadas
                },
            ),
            onClick = onNotifications,
        )
        RadioSection(
            title = stringResource(R.string.ajustes_apariencia),
            options = AppTheme.entries.map { option ->
                SettingsOption(
                    text = themeLabel(option),
                    selected = theme == option,
                    onClick = { onTheme(option) },
                )
            },
        )
        RadioSection(
            title = stringResource(R.string.ajustes_accesibilidad),
            subtitle = stringResource(R.string.ajustes_tamano_texto),
            options = TextSize.entries.map { option ->
                SettingsOption(
                    text = textSizeLabel(option),
                    selected = textSize == option,
                    onClick = { onTextSize(option) },
                )
            },
        )
        SettingsRow(
            title = stringResource(R.string.ajustes_borrar),
            onClick = onRequestDeletion,
            showChevron = false,
            titleColor = MaterialTheme.colorScheme.error,
        )
        AboutCard(version = version)
        if (confirmDeletion) {
            AlertDialog(
                onDismissRequest = onCancelDeletion,
                title = { Text(text = stringResource(R.string.ajustes_borrar_titulo)) },
                text = { Text(text = stringResource(R.string.ajustes_borrar_mensaje)) },
                confirmButton = {
                    TextButton(onClick = onClearData) {
                        Text(
                            text = stringResource(R.string.ajustes_borrar_confirmar),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = onCancelDeletion) {
                        Text(text = stringResource(R.string.ajustes_cancelar))
                    }
                },
            )
        }
    }
}

private data class SettingsOption(
    val text: String,
    val selected: Boolean,
    val onClick: () -> Unit,
)

@Composable
private fun RadioSection(
    title: String,
    options: List<SettingsOption>,
    subtitle: String? = null,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
            Text(
                text = title,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = option.onClick)
                        .padding(end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = option.selected,
                        onClick = option.onClick,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) {
                                colorResource(R.color.azulfer_claro)
                            } else {
                                colorResource(R.color.azulfer)
                            },
                        ),
                    )
                    Text(
                        text = option.text,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun themeLabel(theme: AppTheme): String {
    return when (theme) {
        AppTheme.CLARO -> stringResource(R.string.tema_claro)
        AppTheme.OSCURO -> stringResource(R.string.tema_oscuro)
        AppTheme.SISTEMA -> stringResource(R.string.tema_sistema)
    }
}

@Composable
private fun textSizeLabel(size: TextSize): String {
    return when (size) {
        TextSize.NORMAL -> stringResource(R.string.tamano_normal)
        TextSize.GRANDE -> stringResource(R.string.tamano_grande)
        TextSize.MUY_GRANDE -> stringResource(R.string.tamano_muy_grande)
    }
}

@Composable
private fun SettingsRow(
    title: String,
    onClick: () -> Unit,
    subtitle: String? = null,
    showChevron: Boolean = true,
    titleColor: Color = Color.Unspecified,
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
                    text = title,
                    color = titleColor,
                    style = MaterialTheme.typography.bodyLarge,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            if (showChevron) {
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
private fun AboutCard(version: String) {
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

private fun appVersion(context: Context): String {
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

private fun notificationsEnabled(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}

private fun canAskNotificationPermissionAgain(context: Context): Boolean {
    val activity = context.findActivity() ?: return false
    return activity.shouldShowRequestPermissionRationale(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )
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

private fun openNotificationSettings(context: Context) {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent(AndroidSettings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(AndroidSettings.EXTRA_APP_PACKAGE, context.packageName)
        }
    } else {
        Intent(AndroidSettings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    FERPantallasTheme {
        SettingsContent(
            notificationsOn = true,
            theme = AppTheme.SISTEMA,
            onTheme = {},
            textSize = TextSize.NORMAL,
            onTextSize = {},
            onAboutYou = {},
            onNotifications = {},
            onClearData = {},
            confirmDeletion = false,
            onRequestDeletion = {},
            onCancelDeletion = {},
            version = "1.0",
        )
    }
}
