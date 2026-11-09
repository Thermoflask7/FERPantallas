package zaragoza.mateo.ferpantallas.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import zaragoza.mateo.ferpantallas.modelos.Pregunta
class FaqViewModel : ViewModel(){
    private val preguntas = listOf(
        Pregunta(1, "¿Qué niveles educativos cuentan con becas?", "Aspirantes"),
        Pregunta(2, "¿Cómo puedo solicitar una beca?", "Aspirantes"),
        Pregunta(3, "¿Dónde puedo consultar las convocatorias y resultados?", "Aspirantes"),
        Pregunta(4, "¿Cuál es el proceso de renovación de becas?", "Becarios"),
        Pregunta(5, "¿Cómo puedo saber si mi renovación fue aprobada?","Becarios")
    )

    var selectedFilter by mutableStateOf("Todas")
        private set

    val preguntasfiltradas: List<Pregunta>
        get() = if (selectedFilter == "Todas") preguntas
                else preguntas.filter { it.category == selectedFilter }

    fun onFilterSelected(filter: String){
        selectedFilter = filter
    }
}
