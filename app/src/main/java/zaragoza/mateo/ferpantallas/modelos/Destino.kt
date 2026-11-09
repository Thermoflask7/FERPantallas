package zaragoza.mateo.ferpantallas.modelos

enum class Destino(
    val muestraEncabezado: Boolean,
    val muestraBarra: Boolean,
) {
    BIENVENIDA(muestraEncabezado = false, muestraBarra = false),
    FEED(muestraEncabezado = true, muestraBarra = true),
    LIKED(muestraEncabezado = true, muestraBarra = true),
    POST_DETAIL(muestraEncabezado = false, muestraBarra = false),
    EVENT_DETAIL(muestraEncabezado = false, muestraBarra = false),
    PREGUNTAS(muestraEncabezado = true, muestraBarra = true),
    AJUSTES(muestraEncabezado = true, muestraBarra = true),
    SOBRE_TI(muestraEncabezado = false, muestraBarra = false),
}
