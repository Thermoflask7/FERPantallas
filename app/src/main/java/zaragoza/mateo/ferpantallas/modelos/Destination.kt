package zaragoza.mateo.ferpantallas.modelos

// BIENVENIDA, PREGUNTAS, AJUSTES and SOBRE_TI are saved with the open screen.
enum class Destination(
    val showsHeader: Boolean,
    val showsBottomBar: Boolean,
) {
    BIENVENIDA(showsHeader = false, showsBottomBar = false),
    FEED(showsHeader = true, showsBottomBar = true),
    LIKED(showsHeader = true, showsBottomBar = true),
    POST_DETAIL(showsHeader = false, showsBottomBar = false),
    EVENT_DETAIL(showsHeader = false, showsBottomBar = false),
    PREGUNTAS(showsHeader = true, showsBottomBar = true),
    AJUSTES(showsHeader = true, showsBottomBar = true),
    SOBRE_TI(showsHeader = false, showsBottomBar = false),
}
