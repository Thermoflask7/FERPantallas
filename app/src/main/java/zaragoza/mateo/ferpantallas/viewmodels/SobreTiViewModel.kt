package zaragoza.mateo.ferpantallas.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import zaragoza.mateo.ferpantallas.modelos.CategoriaAviso
import zaragoza.mateo.ferpantallas.modelos.NivelEducativo
import zaragoza.mateo.ferpantallas.modelos.PerfilBeca

class SobreTiViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencias = application.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE)

    var bienvenidaCompleta by mutableStateOf(preferencias.contains(CLAVE_PERFIL))
        private set

    var nivel by mutableStateOf(leerNivel())
        private set

    var perfil by mutableStateOf(leerPerfil())
        private set

    var avisos by mutableStateOf(avisosIniciales())
        private set

    fun guardarBienvenida(perfil: PerfilBeca, nivel: NivelEducativo) {
        this.perfil = perfil
        this.nivel = nivel
        preferencias.edit()
            .putString(CLAVE_PERFIL, perfil.name)
            .putString(CLAVE_NIVEL, nivel.name)
            .apply()
        bienvenidaCompleta = true
    }

    fun restablecer() {
        nivel = NivelEducativo.UNIVERSIDAD
        perfil = PerfilBeca.ASPIRANTE
        avisos = avisosIniciales()
        bienvenidaCompleta = false
        preferencias.edit().remove(CLAVE_PERFIL).remove(CLAVE_NIVEL).apply()
    }

    fun onNivelSeleccionado(nivel: NivelEducativo) {
        this.nivel = nivel
        if (bienvenidaCompleta) {
            preferencias.edit().putString(CLAVE_NIVEL, nivel.name).apply()
        }
    }

    fun onPerfilSeleccionado(perfil: PerfilBeca) {
        this.perfil = perfil
        if (bienvenidaCompleta) {
            preferencias.edit().putString(CLAVE_PERFIL, perfil.name).apply()
        }
    }

    fun onAvisoCambiado(aviso: CategoriaAviso) {
        avisos = if (aviso in avisos) avisos - aviso else avisos + aviso
    }

    private fun leerNivel(): NivelEducativo {
        val guardado = preferencias.getString(CLAVE_NIVEL, null)
        return NivelEducativo.entries.firstOrNull { it.name == guardado }
            ?: NivelEducativo.UNIVERSIDAD
    }

    private fun leerPerfil(): PerfilBeca {
        val guardado = preferencias.getString(CLAVE_PERFIL, null)
        return PerfilBeca.entries.firstOrNull { it.name == guardado }
            ?: PerfilBeca.BECADO
    }

    private companion object {
        const val ARCHIVO = "ajustes"
        const val CLAVE_PERFIL = "perfil"
        const val CLAVE_NIVEL = "nivel"

        fun avisosIniciales(): Set<CategoriaAviso> {
            return setOf(
                CategoriaAviso.CONVOCATORIAS,
                CategoriaAviso.RENOVACION,
                CategoriaAviso.EVENTOS_SOCIALES,
                CategoriaAviso.ANUNCIOS,
            )
        }
    }
}
