package zaragoza.mateo.ferpantallas.vistas

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zaragoza.mateo.ferpantallas.R
import zaragoza.mateo.ferpantallas.modelos.NivelEducativo
import zaragoza.mateo.ferpantallas.modelos.PerfilBeca
import zaragoza.mateo.ferpantallas.ui.theme.FERPantallasTheme

private val AzulArriba = Color(0xFF1F5796)
private val AzulMedio = Color(0xFF2A4C9E)
private val Indigo = Color(0xFF3A37A6)
private val AzulClaro = Color(0xFF3A5EBC)
private val AzulProfundo = Color(0xFF2A4596)
private val TextoMarino = Color(0xFF1E2C7C)
private val Subtitulo = Color(0xFFC5CCF2)

private const val TOTAL_PASOS = 2

private val Montserrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
)

private val CormorantSC = FontFamily(
    Font(R.font.cormorant_sc_medium, FontWeight.Medium),
)

@Composable
fun Bienvenida(
    onContinuar: (PerfilBeca, NivelEducativo) -> Unit,
) {
    var paso by rememberSaveable { mutableIntStateOf(0) }
    var perfilNombre by rememberSaveable { mutableStateOf("") }
    var nivelNombre by rememberSaveable { mutableStateOf("") }
    val perfil = PerfilBeca.entries.firstOrNull { it.name == perfilNombre }
    val nivel = NivelEducativo.entries.firstOrNull { it.name == nivelNombre }

    BackHandler(enabled = paso > 0) { paso -= 1 }

    FondoBienvenida {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(32.dp))
            LogoBienvenida()
            Spacer(Modifier.height(28.dp))
            Text(
                text = stringResource(R.string.bienvenida_titulo),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    fontSize = 42.sp,
                    color = Color.White,
                ),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.bienvenida_subtitulo),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontFamily = Montserrat,
                    fontSize = 17.sp,
                    color = Subtitulo,
                    letterSpacing = 0.3.sp,
                ),
            )
            Spacer(Modifier.height(24.dp))
            IndicadorPasos(total = TOTAL_PASOS, actual = paso)
            Spacer(Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedContent(
                    targetState = paso,
                    modifier = Modifier.fillMaxWidth(),
                    transitionSpec = {
                        val direccion = if (targetState > initialState) 1 else -1
                        (slideInHorizontally { it * direccion / 3 } + fadeIn()) togetherWith
                                (slideOutHorizontally { -it * direccion / 3 } + fadeOut())
                    },
                    label = "pasoBienvenida",
                ) { actual ->
                    if (actual == 0) {
                        PreguntaOpciones(
                            pregunta = stringResource(R.string.bienvenida_pregunta_perfil),
                            opciones = PerfilBeca.entries.map { opcion ->
                                OpcionBienvenida(
                                    texto = textoPerfil(opcion),
                                    seleccionada = opcion == perfil,
                                    onClick = { perfilNombre = opcion.name },
                                )
                            },
                        )
                    } else {
                        PreguntaOpciones(
                            pregunta = stringResource(R.string.bienvenida_pregunta_nivel),
                            opciones = NivelEducativo.entries.map { opcion ->
                                OpcionBienvenida(
                                    texto = textoNivel(opcion),
                                    seleccionada = opcion == nivel,
                                    onClick = { nivelNombre = opcion.name },
                                )
                            },
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            val puedeContinuar = if (paso == 0) perfil != null else nivel != null
            BotonContinuar(
                habilitado = puedeContinuar,
                onClick = {
                    val perfilElegido = perfil
                    val nivelElegido = nivel
                    if (paso == 0) {
                        paso = 1
                    } else if (perfilElegido != null && nivelElegido != null) {
                        onContinuar(perfilElegido, nivelElegido)
                    }
                },
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FondoBienvenida(
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to AzulArriba,
                    0.35f to AzulMedio,
                    0.65f to Indigo,
                    1f to Indigo,
                ),
            )
            .drawBehind {
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(AzulClaro, Color.Transparent),
                        center = Offset(size.width * 0.95f, size.height * 0.5f),
                        radius = size.width * 0.9f,
                    ),
                )
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(AzulProfundo, Color.Transparent),
                        center = Offset(size.width * 0.85f, size.height * 0.98f),
                        radius = size.width * 0.6f,
                    ),
                )
                // Círculos suaves decorativos
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    radius = size.width * 0.55f,
                    center = Offset(size.width * 1.05f, size.height * 0.04f),
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.04f),
                    radius = size.width * 0.4f,
                    center = Offset(-size.width * 0.1f, size.height * 0.88f),
                )
            },
        content = content,
    )
}

@Composable
private fun LogoBienvenida() {
    val linea = Color.White.copy(alpha = 0.7f)
    val estilo = TextStyle(
        fontFamily = CormorantSC,
        fontWeight = FontWeight.Medium,
        fontSize = 34.sp,
        lineHeight = 30.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.width(72.dp).height(1.dp).background(linea))
        Spacer(Modifier.height(8.dp))
        Text(text = stringResource(R.string.bienvenida_logo_fundacion), style = estilo)
        Text(text = stringResource(R.string.bienvenida_logo_esposos), style = estilo)
        Text(
            text = stringResource(R.string.bienvenida_logo_rodriguez),
            style = estilo.copy(fontSize = 38.sp),
        )
        Spacer(Modifier.height(8.dp))
        Box(Modifier.width(72.dp).height(1.dp).background(linea))
    }
}

@Composable
private fun IndicadorPasos(total: Int, actual: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(total) { indice ->
            val ancho by animateDpAsState(
                targetValue = if (indice == actual) 28.dp else 8.dp,
                label = "anchoPaso",
            )
            val color by animateColorAsState(
                targetValue = if (indice <= actual) Color.White else Color.White.copy(alpha = 0.3f),
                label = "colorPaso",
            )
            Box(
                Modifier
                    .height(8.dp)
                    .width(ancho)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}

private data class OpcionBienvenida(
    val texto: String,
    val seleccionada: Boolean,
    val onClick: () -> Unit,
)

@Composable
private fun PreguntaOpciones(
    pregunta: String,
    opciones: List<OpcionBienvenida>,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = pregunta,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.White,
            ),
            modifier = Modifier.padding(bottom = 6.dp),
        )
        opciones.forEach { opcion ->
            PastillaOpcion(
                texto = opcion.texto,
                seleccionada = opcion.seleccionada,
                onClick = opcion.onClick,
            )
        }
    }
}

@Composable
private fun PastillaOpcion(
    texto: String,
    seleccionada: Boolean,
    onClick: () -> Unit,
) {
    val fondo by animateColorAsState(
        targetValue = if (seleccionada) Color.White else Color.White.copy(alpha = 0.12f),
        label = "fondoOpcion",
    )
    val colorTexto by animateColorAsState(
        targetValue = if (seleccionada) TextoMarino else Color.White,
        label = "textoOpcion",
    )
    val colorBorde by animateColorAsState(
        targetValue = if (seleccionada) Color.White else Color.White.copy(alpha = 0.35f),
        label = "bordeOpcion",
    )
    val forma = RoundedCornerShape(50)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(forma)
            .background(fondo)
            .border(1.dp, colorBorde, forma)
            .selectable(
                selected = seleccionada,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = texto,
            style = TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp,
                letterSpacing = 0.4.sp,
                color = colorTexto,
            ),
        )
        AnimatedVisibility(
            visible = seleccionada,
            modifier = Modifier.align(Alignment.CenterEnd),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = TextoMarino,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun BotonContinuar(
    habilitado: Boolean,
    onClick: () -> Unit,
) {
    val alpha by animateFloatAsState(
        targetValue = if (habilitado) 1f else 0.4f,
        label = "alphaContinuar",
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .alpha(alpha)
            .fillMaxWidth()
            .height(54.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable(enabled = habilitado, role = Role.Button, onClick = onClick),
    ) {
        Text(
            text = stringResource(R.string.bienvenida_continuar),
            style = TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = TextoMarino,
                letterSpacing = 0.3.sp,
            ),
        )
        Spacer(Modifier.width(12.dp))
        FlechaDerecha(color = TextoMarino, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun FlechaDerecha(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = size.width * 0.14f
        val medio = size.height / 2
        val punta = size.width * 0.92f
        val cabeza = size.width * 0.4f
        drawLine(
            color,
            Offset(size.width * 0.08f, medio),
            Offset(punta, medio),
            stroke,
            StrokeCap.Round,
        )
        drawLine(
            color,
            Offset(punta - cabeza, medio - cabeza),
            Offset(punta, medio),
            stroke,
            StrokeCap.Round,
        )
        drawLine(
            color,
            Offset(punta - cabeza, medio + cabeza),
            Offset(punta, medio),
            stroke,
            StrokeCap.Round,
        )
    }
}

@Composable
private fun textoPerfil(perfil: PerfilBeca): String {
    return when (perfil) {
        PerfilBeca.ASPIRANTE -> stringResource(R.string.perfil_aspirante)
        PerfilBeca.BECADO -> stringResource(R.string.perfil_becado)
    }
}

@Composable
private fun textoNivel(nivel: NivelEducativo): String {
    return when (nivel) {
        NivelEducativo.PREPARATORIA -> stringResource(R.string.nivel_preparatoria)
        NivelEducativo.UNIVERSIDAD -> stringResource(R.string.nivel_universidad)
        NivelEducativo.POSGRADO -> stringResource(R.string.nivel_posgrado)
    }
}

@Preview(showBackground = true)
@Composable
private fun BienvenidaPreview() {
    FERPantallasTheme {
        Bienvenida(onContinuar = { _, _ -> })
    }
}