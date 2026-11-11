package zaragoza.mateo.ferpantallas.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import zaragoza.mateo.ferpantallas.R
import zaragoza.mateo.ferpantallas.modelos.NoticeCategory
import zaragoza.mateo.ferpantallas.modelos.EducationLevel
import zaragoza.mateo.ferpantallas.modelos.ScholarshipProfile
import zaragoza.mateo.ferpantallas.ui.theme.FERPantallasTheme
import zaragoza.mateo.ferpantallas.viewmodels.AboutYouViewModel

@Composable
fun AboutYou(
    onBack: () -> Unit,
    viewModel: AboutYouViewModel = viewModel(),
) {
    AboutYouContent(
        level = viewModel.level,
        profile = viewModel.profile,
        notices = viewModel.notices,
        onBack = onBack,
        onLevel = viewModel::onLevelSelected,
        onProfile = viewModel::onProfileSelected,
        onNotice = viewModel::onNoticeToggled,
    )
}

@Composable
fun AboutYouContent(
    level: EducationLevel,
    profile: ScholarshipProfile,
    notices: Set<NoticeCategory>,
    onBack: () -> Unit,
    onLevel: (EducationLevel) -> Unit,
    onProfile: (ScholarshipProfile) -> Unit,
    onNotice: (NoticeCategory) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.azulfer))
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_volver),
                    tint = colorResource(R.color.white),
                )
            }
            Text(
                text = stringResource(R.string.sobre_ti_titulo),
                color = colorResource(R.color.white),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            OptionSection(
                title = stringResource(R.string.sobre_ti_nivel),
                options = EducationLevel.entries.map { option ->
                    AboutYouOption(
                        text = levelLabel(option),
                        selected = option == level,
                        onClick = { onLevel(option) },
                    )
                },
            )
            OptionSection(
                title = stringResource(R.string.sobre_ti_perfil),
                options = ScholarshipProfile.entries.map { option ->
                    AboutYouOption(
                        text = profileLabel(option),
                        selected = option == profile,
                        onClick = { onProfile(option) },
                    )
                },
            )
            OptionSection(
                title = stringResource(R.string.sobre_ti_avisos),
                options = NoticeCategory.entries.map { option ->
                    AboutYouOption(
                        text = noticeLabel(option),
                        selected = option in notices,
                        onClick = { onNotice(option) },
                        multiSelect = true,
                    )
                },
            )
        }
    }
}

@Composable
private fun OptionSection(
    title: String,
    options: List<AboutYouOption>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = option.onClick)
                    .semantics {
                        role = if (option.multiSelect) Role.Checkbox else Role.RadioButton
                        selected = option.selected
                    }
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = option.text,
                    style = MaterialTheme.typography.bodyLarge,
                )
                OptionCircle(isSelected = option.selected)
            }
        }
    }
}

private data class AboutYouOption(
    val text: String,
    val selected: Boolean,
    val onClick: () -> Unit,
    val multiSelect: Boolean = false,
)

@Composable
private fun OptionCircle(isSelected: Boolean) {
    val markColor = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) {
        colorResource(R.color.azulfer_claro)
    } else {
        colorResource(R.color.azulfer)
    }
    if (isSelected) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .border(width = 2.dp, color = markColor, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(markColor),
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
        )
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

@Composable
private fun profileLabel(profile: ScholarshipProfile): String {
    return when (profile) {
        ScholarshipProfile.ASPIRANTE -> stringResource(R.string.perfil_aspirante)
        ScholarshipProfile.BECADO -> stringResource(R.string.perfil_becado)
    }
}

@Composable
private fun noticeLabel(notice: NoticeCategory): String {
    return when (notice) {
        NoticeCategory.CONVOCATORIAS -> stringResource(R.string.aviso_convocatorias)
        NoticeCategory.RENOVACION -> stringResource(R.string.aviso_renovacion)
        NoticeCategory.EVENTOS_SOCIALES -> stringResource(R.string.aviso_eventos)
        NoticeCategory.ANUNCIOS -> stringResource(R.string.aviso_anuncios)
    }
}

@Preview(showBackground = true)
@Composable
fun AboutYouPreview() {
    FERPantallasTheme {
        AboutYouContent(
            level = EducationLevel.UNIVERSIDAD,
            profile = ScholarshipProfile.BECADO,
            notices = setOf(
                NoticeCategory.RENOVACION,
                NoticeCategory.EVENTOS_SOCIALES,
                NoticeCategory.ANUNCIOS,
            ),
            onBack = {},
            onLevel = {},
            onProfile = {},
            onNotice = {},
        )
    }
}
