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
import zaragoza.mateo.ferpantallas.modelos.CategoriaAviso
import zaragoza.mateo.ferpantallas.modelos.NivelEducativo
import zaragoza.mateo.ferpantallas.modelos.PerfilBeca
import zaragoza.mateo.ferpantallas.ui.theme.FERPantallasTheme
import zaragoza.mateo.ferpantallas.viewmodels.SobreTiViewModel

@Composable
fun SobreTi(
    onBack: () -> Unit,
    viewModel: SobreTiViewModel = viewModel(),
) {
    SobreTiContent(
        nivel = viewModel.nivel,
        perfil = viewModel.perfil,
        avisos = viewModel.avisos,
        onBack = onBack,
        onNivel = viewModel::onNivelSeleccionado,
        onPerfil = viewModel::onPerfilSeleccionado,
        onAviso = viewModel::onAvisoCambiado,
    )
}

@Composable
fun SobreTiContent(
    nivel: NivelEducativo,
    perfil: PerfilBeca,
    avisos: Set<CategoriaAviso>,
    onBack: () -> Unit,
    onNivel: (NivelEducativo) -> Unit,
    onPerfil: (PerfilBeca) -> Unit,
    onAviso: (CategoriaAviso) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white)),
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
            SeccionOpcion(
                titulo = stringResource(R.string.sobre_ti_nivel),
                opciones = NivelEducativo.entries.map { opcion ->
                    OpcionSobreTi(
                        texto = textoNivel(opcion),
                        seleccionada = opcion == nivel,
                        onClick = { onNivel(opcion) },
                    )
                },
            )
            SeccionOpcion(
                titulo = stringResource(R.string.sobre_ti_perfil),
                opciones = PerfilBeca.entries.map { opcion ->
                    OpcionSobreTi(
                        texto = textoPerfil(opcion),
                        seleccionada = opcion == perfil,
                        onClick = { onPerfil(opcion) },
                    )
                },
            )
            SeccionOpcion(
                titulo = stringResource(R.string.sobre_ti_avisos),
                opciones = CategoriaAviso.entries.map { opcion ->
                    OpcionSobreTi(
                        texto = textoAviso(opcion),
                        seleccionada = opcion in avisos,
                        onClick = { onAviso(opcion) },
                        varias = true,
                    )
                },
            )
        }
    }
}

@Composable
private fun SeccionOpcion(
    titulo: String,
    opciones: List<OpcionSobreTi>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        opciones.forEach { opcion ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = opcion.onClick)
                    .semantics {
                        role = if (opcion.varias) Role.Checkbox else Role.RadioButton
                        selected = opcion.seleccionada
                    }
                    .padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = opcion.texto,
                    style = MaterialTheme.typography.bodyLarge,
                )
                CirculoOpcion(seleccionado = opcion.seleccionada)
            }
        }
    }
}

private data class OpcionSobreTi(
    val texto: String,
    val seleccionada: Boolean,
    val onClick: () -> Unit,
    val varias: Boolean = false,
)

@Composable
private fun CirculoOpcion(seleccionado: Boolean) {
    val azul = colorResource(R.color.azulfer)
    if (seleccionado) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .border(width = 2.dp, color = azul, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(azul),
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
private fun textoNivel(nivel: NivelEducativo): String {
    return when (nivel) {
        NivelEducativo.PRIMARIA -> stringResource(R.string.nivel_primaria)
        NivelEducativo.SECUNDARIA -> stringResource(R.string.nivel_secundaria)
        NivelEducativo.PREPARATORIA -> stringResource(R.string.nivel_preparatoria)
        NivelEducativo.UNIVERSIDAD -> stringResource(R.string.nivel_universidad)
        NivelEducativo.POSGRADO -> stringResource(R.string.nivel_posgrado)
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
private fun textoAviso(aviso: CategoriaAviso): String {
    return when (aviso) {
        CategoriaAviso.CONVOCATORIAS -> stringResource(R.string.aviso_convocatorias)
        CategoriaAviso.RENOVACION -> stringResource(R.string.aviso_renovacion)
        CategoriaAviso.EVENTOS_SOCIALES -> stringResource(R.string.aviso_eventos)
        CategoriaAviso.ANUNCIOS -> stringResource(R.string.aviso_anuncios)
    }
}

@Preview(showBackground = true)
@Composable
fun SobreTiPreview() {
    FERPantallasTheme {
        SobreTiContent(
            nivel = NivelEducativo.UNIVERSIDAD,
            perfil = PerfilBeca.BECADO,
            avisos = setOf(
                CategoriaAviso.RENOVACION,
                CategoriaAviso.EVENTOS_SOCIALES,
                CategoriaAviso.ANUNCIOS,
            ),
            onBack = {},
            onNivel = {},
            onPerfil = {},
            onAviso = {},
        )
    }
}
