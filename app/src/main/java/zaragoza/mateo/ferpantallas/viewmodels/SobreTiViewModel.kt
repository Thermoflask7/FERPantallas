package zaragoza.mateo.ferpantallas.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import zaragoza.mateo.ferpantallas.modelos.CategoriaAviso
import zaragoza.mateo.ferpantallas.modelos.NivelEducativo
import zaragoza.mateo.ferpantallas.modelos.PerfilBeca

class SobreTiViewModel : ViewModel() {
    var nivel by mutableStateOf(NivelEducativo.UNIVERSIDAD)
        private set

    var perfil by mutableStateOf(PerfilBeca.BECADO)
        private set

    var avisos by mutableStateOf(avisosIniciales())
        private set

    fun restablecer() {
        nivel = NivelEducativo.UNIVERSIDAD
        perfil = PerfilBeca.ASPIRANTE
        avisos = avisosIniciales()
    }

    fun onNivelSeleccionado(nivel: NivelEducativo) {
        this.nivel = nivel
    }

    fun onPerfilSeleccionado(perfil: PerfilBeca) {
        this.perfil = perfil
    }

    fun onAvisoCambiado(aviso: CategoriaAviso) {
        avisos = if (aviso in avisos) avisos - aviso else avisos + aviso
    }

    private companion object {
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
