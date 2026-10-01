package plat.lab7.ui.locations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import plat.lab7.data.Location
import plat.lab7.data.LocationDb
import plat.lab7.ui.theme.Lab7Theme

@Composable
fun LocationsScreen(
    onLocationClick: (Int) -> Unit
) {
    val locations = LocationDb().getAllLocations()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(locations) { location ->
            LocationItem(
                location = location,
                onClick = {
                    onLocationClick(location.id)
                }
            )
        }
    }
}

@Composable
private fun LocationItem(
    location: Location,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp,
                    vertical = 20.dp
                )
        ) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = location.type,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LocationsScreenPreview() {
    Lab7Theme {
        LocationsScreen(
            onLocationClick = {}
        )
    }
}