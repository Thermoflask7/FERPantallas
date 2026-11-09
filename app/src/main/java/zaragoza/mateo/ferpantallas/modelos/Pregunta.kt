package zaragoza.mateo.ferpantallas.modelos

data class Pregunta (
    val id: Int,
    val text: String,
    val category: String = "Todas"
)

