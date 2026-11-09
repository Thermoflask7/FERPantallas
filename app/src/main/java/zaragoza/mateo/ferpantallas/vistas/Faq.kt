package zaragoza.mateo.faqfundacionespososrodriguez.vistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import zaragoza.mateo.ferpantallas.R
import zaragoza.mateo.ferpantallas.modelos.Pregunta
import zaragoza.mateo.ferpantallas.ui.theme.FERPantallasTheme
import zaragoza.mateo.ferpantallas.viewmodels.FaqViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun Faq(

){
    val viewModel: FaqViewModel = viewModel()
    val preguntas = viewModel.preguntasfiltradas
    val selectedFilter = viewModel.selectedFilter
    var query by rememberSaveable { mutableStateOf("") }
     Column(
        modifier = Modifier.fillMaxSize().padding(top = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ){
        Card(
            modifier = Modifier.fillMaxWidth(),
             shape = RectangleShape,
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.azulfer))
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.fer),
                    contentDescription = "Fundacion Esposos Rodriguez",
                    contentScale = ContentScale.Fit)
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Preguntas Frecuentes",
                style = MaterialTheme.typography.titleLarge,
            )
            OutlinedTextField(
                value = query,
                onValueChange = {query = it},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                placeholder = {Text(stringResource(R.string.buscar_faq))},
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.lupa),
                        contentDescription = "Buscar",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(width = 30.dp, height = 30.dp)
                    )
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = viewModel.selectedFilter == "Todas",
                    onClick = { viewModel.onFilterSelected("Todas") },
                    label = {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text("Todas")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colorResource(id = R.color.azulfer),
                        selectedLabelColor = Color.White,
                        containerColor = Color.Transparent,          // or a light gray if you prefer
                        labelColor = Color.DarkGray                  // dark text when not selected
                    )
                )
                FilterChip(
                    selected = viewModel.selectedFilter == "Aspirantes",
                    onClick = { viewModel.onFilterSelected("Aspirantes") },
                    label = {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text("Aspirantes")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colorResource(id = R.color.azulfer),
                        selectedLabelColor = Color.White,
                        containerColor = Color.Transparent,
                        labelColor = Color.DarkGray
                    )
                )
                FilterChip(
                    selected = viewModel.selectedFilter == "Becarios",
                    onClick = { viewModel.onFilterSelected("Becarios") },
                    label = {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text("Becarios")
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colorResource(id = R.color.azulfer),
                        selectedLabelColor = Color.White,
                        containerColor = Color.Transparent,
                        labelColor = Color.DarkGray
                    )
                )
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
            ){
                Column{
                    preguntas.forEachIndexed { index, pregunta ->
                        PreguntaItem(pregunta = pregunta, onClick = {})
                        if (index < preguntas.size - 1) {
                            HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.azulfer))
            ){
                CompositionLocalProvider(LocalContentColor provides Color.White) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp , vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Default.QuestionMark,
                            contentDescription = "Interrogacion",
                            modifier = Modifier.size(60.dp),
                        )
                        Column(){
                            Text( text = "¿Aun tienes dudas?", style = MaterialTheme.typography.titleLarge)
                            Text( text = "Estamos aqui para orientarte", style = MaterialTheme.typography.titleMedium)
                            Text(text = "Contactanos por medio de:", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "celular: 662 212 1453", style = MaterialTheme.typography.bodyMedium) // mover strings a string.xml
                            Text(text = "correo: info@fer.org.mx.", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
            ){
                Row(
                    modifier = Modifier.fillMaxWidth().
                    padding(horizontal = 16.dp , vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Hogar",
                        modifier = Modifier.size(50.dp),
                        tint = colorResource(id = R.color.azulfer)
                    )
                    Icon(
                        imageVector = Icons.Default.StarBorder,
                        contentDescription = "Corazon",
                        modifier = Modifier.size(50.dp),
                        tint = colorResource(id = R.color.azulfer)
                    )
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calendario",
                        modifier = Modifier.size(50.dp),
                        tint = colorResource(id = R.color.azulfer)
                    )
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Informacion",
                        modifier = Modifier.size(50.dp),
                        tint = colorResource(id = R.color.azulfer)
                    )
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(50.dp),
                        tint = colorResource(id = R.color.azulfer)
                    )
                }
            }
        }
    }
}

@Composable
fun PreguntaItem(pregunta: Pregunta, onClick: () -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp , vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(
            text = pregunta.text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
            )
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "ver más",
            modifier = Modifier.size(24.dp),
            tint = colorResource(id = R.color.azulfer)
        )

    }
}


@Preview(showBackground = true)
@Composable
fun FaqPreview() {
    FERPantallasTheme {
        Faq()
    }
}