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
import zaragoza.mateo.ferpantallas.modelos.EducationLevel
import zaragoza.mateo.ferpantallas.modelos.ScholarshipProfile
import zaragoza.mateo.ferpantallas.ui.theme.FERPantallasTheme

private val TopBlue = Color(0xFF1F5796)
private val MidBlue = Color(0xFF2A4C9E)
private val Indigo = Color(0xFF3A37A6)
private val LightBlue = Color(0xFF3A5EBC)
private val DeepBlue = Color(0xFF2A4596)
private val NavyText = Color(0xFF1E2C7C)
private val SubtitleColor = Color(0xFFC5CCF2)

private const val TOTAL_STEPS = 2

private val Montserrat = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
)

private val CormorantSC = FontFamily(
    Font(R.font.cormorant_sc_medium, FontWeight.Medium),
)

@Composable
fun Welcome(
    onContinue: (ScholarshipProfile, EducationLevel) -> Unit,
) {
    var step by rememberSaveable { mutableIntStateOf(0) }
    var profileName by rememberSaveable { mutableStateOf("") }
    var levelName by rememberSaveable { mutableStateOf("") }
    val profile = ScholarshipProfile.entries.firstOrNull { it.name == profileName }
    val level = EducationLevel.entries.firstOrNull { it.name == levelName }

    BackHandler(enabled = step > 0) { step -= 1 }

    WelcomeBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(32.dp))
            WelcomeLogo()
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
                    color = SubtitleColor,
                    letterSpacing = 0.3.sp,
                ),
            )
            Spacer(Modifier.height(24.dp))
            StepIndicator(total = TOTAL_STEPS, current = step)
            Spacer(Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AnimatedContent(
                    targetState = step,
                    modifier = Modifier.fillMaxWidth(),
                    transitionSpec = {
                        val direction = if (targetState > initialState) 1 else -1
                        (slideInHorizontally { it * direction / 3 } + fadeIn()) togetherWith
                                (slideOutHorizontally { -it * direction / 3 } + fadeOut())
                    },
                    label = "pasoBienvenida",
                ) { current ->
                    if (current == 0) {
                        OptionQuestion(
                            question = stringResource(R.string.bienvenida_pregunta_perfil),
                            options = ScholarshipProfile.entries.map { option ->
                                WelcomeOption(
                                    text = profileLabel(option),
                                    selected = option == profile,
                                    onClick = { profileName = option.name },
                                )
                            },
                        )
                    } else {
                        OptionQuestion(
                            question = stringResource(R.string.bienvenida_pregunta_nivel),
                            options = EducationLevel.entries.map { option ->
                                WelcomeOption(
                                    text = levelLabel(option),
                                    selected = option == level,
                                    onClick = { levelName = option.name },
                                )
                            },
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            val canContinue = if (step == 0) profile != null else level != null
            ContinueButton(
                enabled = canContinue,
                onClick = {
                    val chosenProfile = profile
                    val chosenLevel = level
                    if (step == 0) {
                        step = 1
                    } else if (chosenProfile != null && chosenLevel != null) {
                        onContinue(chosenProfile, chosenLevel)
                    }
                },
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun WelcomeBackground(
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to TopBlue,
                    0.35f to MidBlue,
                    0.65f to Indigo,
                    1f to Indigo,
                ),
            )
            .drawBehind {
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(LightBlue, Color.Transparent),
                        center = Offset(size.width * 0.95f, size.height * 0.5f),
                        radius = size.width * 0.9f,
                    ),
                )
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(DeepBlue, Color.Transparent),
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
private fun WelcomeLogo() {
    val line = Color.White.copy(alpha = 0.7f)
    val style = TextStyle(
        fontFamily = CormorantSC,
        fontWeight = FontWeight.Medium,
        fontSize = 34.sp,
        lineHeight = 30.sp,
        color = Color.White,
        textAlign = TextAlign.Center,
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.width(72.dp).height(1.dp).background(line))
        Spacer(Modifier.height(8.dp))
        Text(text = stringResource(R.string.bienvenida_logo_fundacion), style = style)
        Text(text = stringResource(R.string.bienvenida_logo_esposos), style = style)
        Text(
            text = stringResource(R.string.bienvenida_logo_rodriguez),
            style = style.copy(fontSize = 38.sp),
        )
        Spacer(Modifier.height(8.dp))
        Box(Modifier.width(72.dp).height(1.dp).background(line))
    }
}

@Composable
private fun StepIndicator(total: Int, current: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(total) { index ->
            val width by animateDpAsState(
                targetValue = if (index == current) 28.dp else 8.dp,
                label = "anchoPaso",
            )
            val color by animateColorAsState(
                targetValue = if (index <= current) Color.White else Color.White.copy(alpha = 0.3f),
                label = "colorPaso",
            )
            Box(
                Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}

private data class WelcomeOption(
    val text: String,
    val selected: Boolean,
    val onClick: () -> Unit,
)

@Composable
private fun OptionQuestion(
    question: String,
    options: List<WelcomeOption>,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = question,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                color = Color.White,
            ),
            modifier = Modifier.padding(bottom = 6.dp),
        )
        options.forEach { option ->
            OptionPill(
                text = option.text,
                selected = option.selected,
                onClick = option.onClick,
            )
        }
    }
}

@Composable
private fun OptionPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color.White.copy(alpha = 0.12f),
        label = "fondoOpcion",
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) NavyText else Color.White,
        label = "textoOpcion",
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color.White.copy(alpha = 0.35f),
        label = "bordeOpcion",
    )
    val shape = RoundedCornerShape(50)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(shape)
            .background(backgroundColor)
            .border(1.dp, borderColor, shape)
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp,
                letterSpacing = 0.4.sp,
                color = textColor,
            ),
        )
        AnimatedVisibility(
            visible = selected,
            modifier = Modifier.align(Alignment.CenterEnd),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = NavyText,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun ContinueButton(
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.4f,
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
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
    ) {
        Text(
            text = stringResource(R.string.bienvenida_continuar),
            style = TextStyle(
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = NavyText,
                letterSpacing = 0.3.sp,
            ),
        )
        Spacer(Modifier.width(12.dp))
        RightArrow(color = NavyText, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun RightArrow(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = size.width * 0.14f
        val middle = size.height / 2
        val tip = size.width * 0.92f
        val head = size.width * 0.4f
        drawLine(
            color,
            Offset(size.width * 0.08f, middle),
            Offset(tip, middle),
            stroke,
            StrokeCap.Round,
        )
        drawLine(
            color,
            Offset(tip - head, middle - head),
            Offset(tip, middle),
            stroke,
            StrokeCap.Round,
        )
        drawLine(
            color,
            Offset(tip - head, middle + head),
            Offset(tip, middle),
            stroke,
            StrokeCap.Round,
        )
    }
}

@Composable
private fun profileLabel(profile: ScholarshipProfile): String {
    return when (profile) {
        ScholarshipProfile.ASPIRANTE -> stringResource(R.string.perfil_aspirante)
        ScholarshipProfile.BECADO -> stringResource(R.string.perfil_becado)
    }
}

@Composable
private fun levelLabel(level: EducationLevel): String {
    return when (level) {
        EducationLevel.PREPARATORIA -> stringResource(R.string.nivel_preparatoria)
        EducationLevel.UNIVERSIDAD -> stringResource(R.string.nivel_universidad)
        EducationLevel.POSGRADO -> stringResource(R.string.nivel_posgrado)
    }
}

@Preview(showBackground = true)
@Composable
private fun WelcomePreview() {
    FERPantallasTheme {
        Welcome(onContinue = { _, _ -> })
    }
}