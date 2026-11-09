package zaragoza.mateo.ferpantallas.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import zaragoza.mateo.ferpantallas.modelos.TemaApp

class AjustesViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencias = application.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    var tema by mutableStateOf(leerTema())
        private set

    fun onTemaSeleccionado(tema: TemaApp) {
        this.tema = tema
        preferencias.edit().putString(CLAVE_TEMA, tema.name).apply()
    }

    private fun leerTema(): TemaApp {
        val guardado = preferencias.getString(CLAVE_TEMA, TemaApp.SISTEMA.name)
        return TemaApp.entries.firstOrNull { it.name == guardado } ?: TemaApp.SISTEMA
    }

    private companion object {
        const val ARCHIVO = "ajustes"
        const val CLAVE_TEMA = "tema"
    }
}
