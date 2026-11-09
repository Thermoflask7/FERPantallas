package zaragoza.mateo.ferpantallas.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import zaragoza.mateo.ferpantallas.R
import zaragoza.mateo.ferpantallas.modelos.CategoriaFaq
import zaragoza.mateo.ferpantallas.modelos.Pregunta
import zaragoza.mateo.ferpantallas.modelos.PreguntaVisible

class FaqViewModel(application: Application) : AndroidViewModel(application) {
    private val preguntas = listOf(
        Pregunta(1, R.string.faq_q_niveles, R.string.faq_a_niveles, CategoriaFaq.ASPIRANTES),
        Pregunta(2, R.string.faq_q_solicitar, R.string.faq_a_solicitar, CategoriaFaq.ASPIRANTES),
        Pregunta(3, R.string.faq_q_convocatorias, R.string.faq_a_convocatorias, CategoriaFaq.ASPIRANTES),
        Pregunta(4, R.string.faq_q_renovacion, R.string.faq_a_renovacion, CategoriaFaq.BECARIOS),
        Pregunta(5, R.string.faq_q_renovacion_aprobada, R.string.faq_a_renovacion_aprobada, CategoriaFaq.BECARIOS),
    )

    var selectedFilter by mutableStateOf(CategoriaFaq.TODAS)
        private set

    var query by mutableStateOf("")
        private set

    var selectedPreguntaId by mutableStateOf<Int?>(null)
        private set

    val preguntasFiltradas: List<PreguntaVisible>
        get() {
            val texto = query.trim()
            return preguntas
                .filter { pregunta ->
                    selectedFilter == CategoriaFaq.TODAS || pregunta.category == selectedFilter
                }
                .map { pregunta ->
                    PreguntaVisible(
                        id = pregunta.id,
                        text = getApplication<Application>().getString(pregunta.textRes),
                        answer = getApplication<Application>().getString(pregunta.answerRes),
                    )
                }
                .filter { pregunta ->
                    texto.isEmpty() || pregunta.text.contains(texto, ignoreCase = true)
                }
        }

    fun onFilterSelected(filter: CategoriaFaq) {
        selectedFilter = filter
    }

    fun onQueryChange(value: String) {
        query = value
    }

    fun onPreguntaClick(id: Int) {
        selectedPreguntaId = if (selectedPreguntaId == id) null else id
    }
}
