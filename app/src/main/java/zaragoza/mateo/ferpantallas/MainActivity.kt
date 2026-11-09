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
import zaragoza.mateo.ferpantallas.modelos.TemaApp
import zaragoza.mateo.ferpantallas.ui.theme.FERPantallasTheme
import zaragoza.mateo.ferpantallas.viewmodels.AjustesViewModel
import zaragoza.mateo.ferpantallas.vistas.MarcoApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val ajustesViewModel: AjustesViewModel = viewModel()
            val oscuro = when (ajustesViewModel.tema) {
                TemaApp.CLARO -> false
                TemaApp.OSCURO -> true
                TemaApp.SISTEMA -> isSystemInDarkTheme()
            }
            FERPantallasTheme(
                darkTheme = oscuro,
                escalaTexto = ajustesViewModel.tamanoTexto.escala,
            ) {
                MarcoApp()
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