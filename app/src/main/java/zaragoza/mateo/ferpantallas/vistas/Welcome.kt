package zaragoza.mateo.ferpantallas.vistas

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
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


private val BgTop = Color(0xFF3A6AC0)
private val BgMid = Color(0xFF1B3FA8)
private val BgBottom = Color(0xFF1238BA)
private val BgGlow = Color(0xFF4A82B8)
private val NavyText = Color(0xFF1E2C7C)
private val ButtonBlue = Color(0xFFC9EFF0)
private val ButtonText = Color(0xFF1B3CA8)
private val SubtitleColor = Color(0xFFD5DCF5)
private val SquiggleTeal = Color(0xFF5DBABB)
private val SquiggleBlue = Color(0xFF2F70DF)

private val ContentPadding = 48.dp
private const val TOTAL_STEPS = 3


private val CormorantSC = FontFamily(
    Font(R.font.cormorant_sc_medium, FontWeight.Medium),
)

private const val DESIGN_W = 569f
private const val DESIGN_H = 570f
private const val DESIGN_STROKE = 100f

private fun squigglePath(): Path = Path().apply {
    moveTo(-60f, 127f)
    cubicTo(0f, 104f, 55f, 51f, 116f, 71f)
    cubicTo(172f, 91f, 151f, 140f, 133f, 176f)
    cubicTo(108f, 236f, 85f, 275f, 100f, 311f)
    cubicTo(118f, 351f, 174f, 335f, 236f, 281f)
    cubicTo(304f, 221f, 370f, 121f, 422f, 83f)
    cubicTo(474f, 49f, 524f, 91f, 491f, 151f)
    cubicTo(451f, 241f, 385f, 331f, 372f, 431f)
    cubicTo(366f, 521f, 430f, 520f, 500f, 470f)
    cubicTo(541f, 446f, 600f, 410f, 650f, 385f)
}

@Composable
fun AnimatedSquiggle(
    modifier: Modifier = Modifier,
    durationMillis: Int = 2500,
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis, easing = FastOutSlowInEasing),
        )
    }

    Box(
        modifier = modifier.drawWithCache {
            val scale = size.width / DESIGN_W

            val scaledPath = squigglePath().apply {
                transform(Matrix().apply { scale(scale, scale) })
            }
            val measure = PathMeasure().apply { setPath(scaledPath, false) }
            val length = measure.length
            val segment = Path()

            val brush = Brush.linearGradient(
                colors = listOf(SquiggleTeal, SquiggleBlue),
                start = Offset.Zero,
                end = Offset(size.width, size.height),
            )
            val stroke = Stroke(
                width = DESIGN_STROKE * scale,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
            )

            onDrawBehind {
                segment.reset()
                measure.getSegment(0f, length * progress.value, segment, true)
                drawPath(segment, brush, style = stroke)
            }
        },
    )
}

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
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(24.dp))
            StepIndicator(
                total = TOTAL_STEPS,
                current = step,
                modifier = Modifier.padding(horizontal = ContentPadding),
            )
            Spacer(Modifier.height(40.dp))
            WelcomeLogo()
            Spacer(Modifier.height(16.dp))

            AnimatedContent(
                targetState = step,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                transitionSpec = {
                    val direction = if (targetState > initialState) 1 else -1
                    (slideInHorizontally { it * direction / 3 } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it * direction / 3 } + fadeOut())
                },
                label = "welcomeStep",
            ) { current ->
                when (current) {
                    0 -> IntroBody()
                    1 -> QuestionBody {
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
                    }
                    else -> QuestionBody {
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

            val canContinue = when (step) {
                0 -> true
                1 -> profile != null
                else -> level != null
            }
            ContinueButton(
                text = stringResource(
                    if (step == 0) R.string.bienvenida_comenzar else R.string.bienvenida_continuar,
                ),
                enabled = canContinue,
                onClick = {
                    when (step) {
                        0 -> step = 1
                        1 -> step = 2
                        else -> if (profile != null && level != null) {
                            onContinue(profile, level)
                        }
                    }
                },
                modifier = Modifier.padding(horizontal = ContentPadding),
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun IntroBody() {
    Box(Modifier.fillMaxSize()) {
        AnimatedSquiggle(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(DESIGN_W / DESIGN_H)
                .align(Alignment.TopCenter),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = ContentPadding)
                .padding(bottom = 20.dp),
        ) {
            Text(
                text = stringResource(R.string.bienvenida_titulo),
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 46.sp,
                    color = Color.White,
                ),
            )
            Text(
                text = stringResource(R.string.bienvenida_subtitulo),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = SubtitleColor,
                    letterSpacing = 0.3.sp,
                ),
            )
        }
    }
}

@Composable
private fun QuestionBody(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = ContentPadding)
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
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
                    0f to BgTop,
                    0.45f to BgMid,
                    1f to BgBottom,
                ),
            )
            .drawBehind {
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(BgGlow, Color.Transparent),
                        center = Offset(size.width * 0.95f, size.height * 0.02f),
                        radius = size.width * 0.9f,
                    ),
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
private fun StepIndicator(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(total) { index ->
            val color by animateColorAsState(
                targetValue = if (index <= current) Color.White else Color.White.copy(alpha = 0.3f),
                label = "stepColor",
            )
            Box(
                Modifier
                    .weight(1f)
                    .height(6.dp)
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
        label = "optionBg",
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) NavyText else Color.White,
        label = "optionText",
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) Color.White else Color.White.copy(alpha = 0.35f),
        label = "optionBorder",
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
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.4f,
        label = "continueAlpha",
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .alpha(alpha)
            .fillMaxWidth()
            .height(50.dp)
            .clip(CircleShape)
            .background(Color.White)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = ButtonText,
                letterSpacing = 0.3.sp,
            ),
        )
        Spacer(Modifier.width(12.dp))
        RightArrow(color = ButtonText, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun RightArrow(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = size.width * 0.14f
        val middle = size.height / 2
        val tip = size.width * 0.92f
        val head = size.width * 0.4f
        drawLine(color, Offset(size.width * 0.08f, middle), Offset(tip, middle), stroke, StrokeCap.Round)
        drawLine(color, Offset(tip - head, middle - head), Offset(tip, middle), stroke, StrokeCap.Round)
        drawLine(color, Offset(tip - head, middle + head), Offset(tip, middle), stroke, StrokeCap.Round)
    }
}

@Composable
private fun profileLabel(profile: ScholarshipProfile): String {
    return when (profile) {
        ScholarshipProfile.ASPIRANTE -> stringResource(R.string.perfil_aspirante)
        ScholarshipProfile.BECADO -> stringResource(R.string.perfil_becado)
        ScholarshipProfile.OTROS -> stringResource(R.string.perfil_otros)
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