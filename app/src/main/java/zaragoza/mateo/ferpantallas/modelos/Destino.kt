package zaragoza.mateo.ferpantallas.modelos

enum class Destino(
    val muestraEncabezado: Boolean,
    val muestraBarra: Boolean,
) {
    PREGUNTAS(muestraEncabezado = true, muestraBarra = true),
    AJUSTES(muestraEncabezado = true, muestraBarra = true),
    SOBRE_TI(muestraEncabezado = false, muestraBarra = false),
}
