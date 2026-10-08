
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab7.data.Location
import plat.lab7.data.LocationDb
import plat.lab7.ui.components.ErrorScreen
import plat.lab7.ui.components.LoadingScreen
import plat.lab7.ui.theme.Lab7Theme
import plat.lab7.viewmodel.LocationsUiState
import plat.lab7.viewmodel.LocationsViewModel

@Composable
fun LocationsScreen(
    onLocationClick: (Int) -> Unit,
    locationsViewModel: LocationsViewModel = viewModel()
) {
    val uiState by locationsViewModel.uiState.collectAsStateWithLifecycle()

    LocationsContent(
        uiState = uiState,
        onLocationClick = onLocationClick,
        onLoadingClick = locationsViewModel::showError,
        onRetry = locationsViewModel::loadLocations
    )
}

@Composable
fun LocationsContent(
    uiState: LocationsUiState,
    onLocationClick: (Int) -> Unit,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit
) {
    when {
        uiState.hasError -> {
            ErrorScreen(
                onRetry = onRetry,
                message = "Error al obtener listado de ubicaciones.\nIntenta de nuevo"
            )
        }

        uiState.isLoading -> {
            LoadingScreen(
                onClick = onLoadingClick
            )
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = uiState.data,
                    key = { it.id }
                ) { location ->
                    LocationItem(
                        location = location,
                        onClick = {
                            onLocationClick(location.id)
                        }
                    )
                }
            }
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
fun LocationsLoadingPreview() {
    Lab7Theme {
        LocationsContent(
            uiState = LocationsUiState(
                isLoading = true
            ),
            onLocationClick = {},
            onLoadingClick = {},
            onRetry = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LocationsErrorPreview() {
    Lab7Theme {
        LocationsContent(
            uiState = LocationsUiState(
                isLoading = false,
                hasError = true
            ),
            onLocationClick = {},
            onLoadingClick = {},
            onRetry = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LocationsDataPreview() {
    Lab7Theme {
        LocationsContent(
            uiState = LocationsUiState(
                isLoading = false,
                data = LocationDb().getAllLocations()
            ),
            onLocationClick = {},
            onLoadingClick = {},
            onRetry = {}
        )
    }
}
