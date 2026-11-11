package zaragoza.mateo.ferpantallas.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import zaragoza.mateo.ferpantallas.R
import zaragoza.mateo.ferpantallas.modelos.Destination

@Composable
fun BottomBar(
    destination: Destination,
    onFeed: () -> Unit,
    onLiked: () -> Unit,
    onFaq: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        shape = RoundedCornerShape(50),
        color = Color.White,
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BarIcon(
                icon = Icons.Outlined.Home,
                selectedIcon = Icons.Filled.Home,
                description = stringResource(R.string.cd_nav_inicio),
                isSelected = destination == Destination.FEED,
                onClick = onFeed,
            )
            BarIcon(
                icon = Icons.Outlined.FavoriteBorder,
                selectedIcon = Icons.Filled.Favorite,
                description = stringResource(R.string.cd_nav_favoritos),
                isSelected = destination == Destination.LIKED,
                onClick = onLiked,
            )
            BarIcon(
                icon = Icons.Outlined.Info,
                selectedIcon = Icons.Filled.Info,
                description = stringResource(R.string.cd_nav_info),
                isSelected = destination == Destination.PREGUNTAS,
                onClick = onFaq,
            )
            BarIcon(
                icon = Icons.Outlined.Settings,
                selectedIcon = Icons.Filled.Settings,
                description = stringResource(R.string.cd_nav_ajustes),
                isSelected = destination == Destination.AJUSTES,
                onClick = onSettings,
            )
        }
    }
}

@Composable
private fun BarIcon(
    icon: ImageVector,
    selectedIcon: ImageVector,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF5F5F5F)

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .semantics { selected = isSelected }
            .clickable(role = Role.Tab, onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = if (isSelected) selectedIcon else icon,
            contentDescription = description,
            tint = tint,
            modifier = Modifier.size(30.dp),
        )

        Spacer(Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .width(28.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(50))
                .background(if (isSelected) tint else Color.Transparent),
        )
    }
}