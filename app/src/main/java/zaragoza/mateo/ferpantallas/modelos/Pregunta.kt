package zaragoza.mateo.ferpantallas.modelos

data class Pregunta(
    val id: Int,
    val textRes: Int,
    val answerRes: Int,
    val category: CategoriaFaq,
)
