package zaragoza.mateo.ferpantallas.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import zaragoza.mateo.ferpantallas.R
import zaragoza.mateo.ferpantallas.modelos.FaqCategory
import zaragoza.mateo.ferpantallas.modelos.VisibleQuestion
import zaragoza.mateo.ferpantallas.ui.theme.FERPantallasTheme
import zaragoza.mateo.ferpantallas.viewmodels.FaqViewModel

@Composable
fun Faq(
    viewModel: FaqViewModel = viewModel(),
) {
    FaqContent(
        questions = viewModel.filteredQuestions,
        selectedFilter = viewModel.selectedFilter,
        query = viewModel.query,
        onFilterSelected = viewModel::onFilterSelected,
        onQueryChange = viewModel::onQueryChange,
        selectedQuestionId = viewModel.selectedQuestionId,
        onQuestionClick = viewModel::onQuestionClick,
    )
}

@Composable
fun FaqContent(
    questions: List<VisibleQuestion>,
    selectedFilter: FaqCategory,
    query: String,
    onFilterSelected: (FaqCategory) -> Unit,
    onQueryChange: (String) -> Unit,
    selectedQuestionId: Int?,
    onQuestionClick: (Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.faq_titulo),
                style = MaterialTheme.typography.titleLarge,
            )
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                placeholder = { Text(stringResource(R.string.buscar_faq)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.cd_buscar),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FaqFilterChip(
                    text = stringResource(R.string.filtro_todas),
                    selected = selectedFilter == FaqCategory.TODAS,
                    onClick = { onFilterSelected(FaqCategory.TODAS) },
                )
                FaqFilterChip(
                    text = stringResource(R.string.filtro_aspirantes),
                    selected = selectedFilter == FaqCategory.ASPIRANTES,
                    onClick = { onFilterSelected(FaqCategory.ASPIRANTES) },
                )
                FaqFilterChip(
                    text = stringResource(R.string.filtro_becarios),
                    selected = selectedFilter == FaqCategory.BECARIOS,
                    onClick = { onFilterSelected(FaqCategory.BECARIOS) },
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
            ) {
                Column {
                    questions.forEachIndexed { index, question ->
                        QuestionRow(
                            question = question,
                            expanded = question.id == selectedQuestionId,
                            onClick = { onQuestionClick(question.id) },
                        )
                        if (index < questions.lastIndex) {
                            HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.azulfer)),
            ) {
                CompositionLocalProvider(LocalContentColor provides Color.White) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.QuestionMark,
                            contentDescription = stringResource(R.string.cd_faq_dudas),
                            modifier = Modifier.size(60.dp),
                        )
                        Column {
                            Text(
                                text = stringResource(R.string.faq_dudas_titulo),
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                text = stringResource(R.string.faq_dudas_subtitulo),
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = stringResource(R.string.faq_contacto_medio),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = stringResource(R.string.faq_contacto_celular),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = stringResource(R.string.faq_contacto_correo),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FaqFilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                maxLines = 1,
                softWrap = false,
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = colorResource(id = R.color.azulfer),
            selectedLabelColor = Color.White,
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@Composable
fun QuestionRow(
    question: VisibleQuestion,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    LaunchedEffect(expanded) {
        if (expanded) {
            bringIntoViewRequester.bringIntoView()
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = question.text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
            )
            Icon(
                imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = stringResource(
                    if (expanded) R.string.cd_cerrar_respuesta else R.string.cd_ver_mas,
                ),
                modifier = Modifier.size(24.dp),
                tint = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) {
                    colorResource(R.color.azulfer_claro)
                } else {
                    colorResource(id = R.color.azulfer)
                },
            )
        }
        if (expanded) {
            Text(
                text = question.answer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FaqPreview() {
    FERPantallasTheme {
        FaqContent(
            questions = listOf(
                VisibleQuestion(
                    1,
                    stringResource(R.string.faq_q_niveles),
                    stringResource(R.string.faq_a_niveles),
                ),
                VisibleQuestion(
                    2,
                    stringResource(R.string.faq_q_solicitar),
                    stringResource(R.string.faq_a_solicitar),
                ),
            ),
            selectedFilter = FaqCategory.TODAS,
            query = "",
            onFilterSelected = {},
            onQueryChange = {},
            selectedQuestionId = 1,
            onQuestionClick = {},
        )
    }
}
