package zaragoza.mateo.ferpantallas.modelos

data class Question(
    val id: Int,
    val textRes: Int,
    val answerRes: Int,
    val category: FaqCategory,
)
