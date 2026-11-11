package zaragoza.mateo.ferpantallas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import zaragoza.mateo.ferpantallas.modelos.AppTheme
import zaragoza.mateo.ferpantallas.ui.theme.FERPantallasTheme
import zaragoza.mateo.ferpantallas.viewmodels.SettingsViewModel
import zaragoza.mateo.ferpantallas.vistas.AppFrame

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val dark = when (settingsViewModel.theme) {
                AppTheme.CLARO -> false
                AppTheme.OSCURO -> true
                AppTheme.SISTEMA -> isSystemInDarkTheme()
            }
            FERPantallasTheme(
                darkTheme = dark,
                textScale = settingsViewModel.textSize.scale,
            ) {
                AppFrame()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FERPantallasTheme {
        Greeting("Android")
    }
}