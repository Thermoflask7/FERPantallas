package zaragoza.mateo.ferpantallas.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import zaragoza.mateo.ferpantallas.modelos.TamanoTexto
import zaragoza.mateo.ferpantallas.modelos.TemaApp

class AjustesViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencias = application.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    var tema by mutableStateOf(leerTema())
        private set

    var tamanoTexto by mutableStateOf(leerTamanoTexto())
        private set

    fun onTemaSeleccionado(tema: TemaApp) {
        this.tema = tema
        preferencias.edit().putString(CLAVE_TEMA, tema.name).apply()
    }

    fun onTamanoTextoSeleccionado(tamano: TamanoTexto) {
        tamanoTexto = tamano
        preferencias.edit().putString(CLAVE_TAMANO_TEXTO, tamano.name).apply()
    }

    fun borrarDatos() {
        tema = TemaApp.SISTEMA
        tamanoTexto = TamanoTexto.NORMAL
        preferencias.edit().clear().apply()
    }

    private fun leerTema(): TemaApp {
        val guardado = preferencias.getString(CLAVE_TEMA, TemaApp.SISTEMA.name)
        return TemaApp.entries.firstOrNull { it.name == guardado } ?: TemaApp.SISTEMA
    }

    private fun leerTamanoTexto(): TamanoTexto {
        val guardado = preferencias.getString(CLAVE_TAMANO_TEXTO, TamanoTexto.NORMAL.name)
        return TamanoTexto.entries.firstOrNull { it.name == guardado } ?: TamanoTexto.NORMAL
    }

    private companion object {
        // SobreTiViewModel guarda perfil y nivel en este mismo archivo.
        const val ARCHIVO = "ajustes"
        const val CLAVE_TEMA = "tema"
        const val CLAVE_TAMANO_TEXTO = "tamano_texto"
    }
}
