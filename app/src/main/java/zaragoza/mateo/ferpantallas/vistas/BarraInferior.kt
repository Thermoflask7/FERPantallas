package zaragoza.mateo.ferpantallas.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import zaragoza.mateo.ferpantallas.R
import zaragoza.mateo.ferpantallas.modelos.Destino

@Composable
fun BarraInferior(
    destino: Destino,
    onFeed: () -> Unit,
    onLiked: () -> Unit,
    onPreguntas: () -> Unit,
    onAjustes: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconoBarra(
                icono = Icons.Default.Home,
                descripcion = stringResource(R.string.cd_nav_inicio),
                seleccionado = destino == Destino.FEED,
                onClick = onFeed,
            )
            IconoBarra(
                icono = Icons.Default.StarBorder,
                descripcion = stringResource(R.string.cd_nav_favoritos),
                seleccionado = destino == Destino.LIKED,
                onClick = onLiked,
            )
            IconoBarra(
                icono = Icons.Default.CalendarMonth,
                descripcion = stringResource(R.string.cd_nav_calendario),
                seleccionado = false,
            )
            IconoBarra(
                icono = Icons.Default.Info,
                descripcion = stringResource(R.string.cd_nav_info),
                seleccionado = destino == Destino.PREGUNTAS,
                onClick = onPreguntas,
            )
            IconoBarra(
                icono = Icons.Default.Settings,
                descripcion = stringResource(R.string.cd_nav_ajustes),
                seleccionado = destino == Destino.AJUSTES,
                onClick = onAjustes,
            )
        }
    }
}

@Composable
private fun IconoBarra(
    icono: ImageVector,
    descripcion: String,
    seleccionado: Boolean,
    onClick: (() -> Unit)? = null,
) {
    val azul = colorResource(R.color.azulfer)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            modifier = Modifier
                .size(50.dp)
                .then(
                    if (onClick == null) {
                        Modifier
                    } else {
                        Modifier
                            .clickable(onClick = onClick)
                            .semantics {
                                role = Role.Tab
                                selected = seleccionado
                            }
                    },
                ),
            tint = azul,
        )
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .width(28.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (seleccionado) azul else Color.Transparent),
        )
    }
}
