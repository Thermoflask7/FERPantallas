package zaragoza.mateo.ferpantallas.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import zaragoza.mateo.ferpantallas.R
import zaragoza.mateo.ferpantallas.modelos.FaqCategory
import zaragoza.mateo.ferpantallas.modelos.Question
import zaragoza.mateo.ferpantallas.modelos.VisibleQuestion

class FaqViewModel(application: Application) : AndroidViewModel(application) {
    private val questions = listOf(
        Question(1, R.string.faq_q_niveles, R.string.faq_a_niveles, FaqCategory.ASPIRANTES),
        Question(2, R.string.faq_q_solicitar, R.string.faq_a_solicitar, FaqCategory.ASPIRANTES),
        Question(3, R.string.faq_q_convocatorias, R.string.faq_a_convocatorias, FaqCategory.ASPIRANTES),
        Question(4, R.string.faq_q_renovacion, R.string.faq_a_renovacion, FaqCategory.BECARIOS),
        Question(5, R.string.faq_q_renovacion_aprobada, R.string.faq_a_renovacion_aprobada, FaqCategory.BECARIOS),
    )

    var selectedFilter by mutableStateOf(FaqCategory.TODAS)
        private set

    var query by mutableStateOf("")
        private set

    var selectedQuestionId by mutableStateOf<Int?>(null)
        private set

    val filteredQuestions: List<VisibleQuestion>
        get() {
            val text = query.trim()
            return questions
                .filter { question ->
                    selectedFilter == FaqCategory.TODAS || question.category == selectedFilter
                }
                .map { question ->
                    VisibleQuestion(
                        id = question.id,
                        text = getApplication<Application>().getString(question.textRes),
                        answer = getApplication<Application>().getString(question.answerRes),
                    )
                }
                .filter { question ->
                    text.isEmpty() || question.text.contains(text, ignoreCase = true)
                }
        }

    fun onFilterSelected(filter: FaqCategory) {
        selectedFilter = filter
    }

    fun onQueryChange(value: String) {
        query = value
    }

    fun onQuestionClick(id: Int) {
        selectedQuestionId = if (selectedQuestionId == id) null else id
    }
}
