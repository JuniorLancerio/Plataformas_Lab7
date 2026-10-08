
package plat.lab7.ui.locations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab7.data.LocationDb
import plat.lab7.ui.components.ErrorScreen
import plat.lab7.ui.components.LoadingScreen
import plat.lab7.ui.theme.Lab7Theme
import plat.lab7.viewmodel.LocationDetailsUiState
import plat.lab7.viewmodel.LocationDetailsViewModel

@Composable
fun LocationDetailsScreen(
    onBack: () -> Unit,
    locationDetailsViewModel: LocationDetailsViewModel = viewModel()
) {
    val uiState by locationDetailsViewModel.uiState.collectAsStateWithLifecycle()

    LocationDetailsContent(
        uiState = uiState,
        onBack = onBack,
        onLoadingClick = locationDetailsViewModel::showError,
        onRetry = locationDetailsViewModel::loadLocation
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsContent(
    uiState: LocationDetailsUiState,
    onBack: () -> Unit,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Location details")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->

        when {
            uiState.hasError -> {
                ErrorScreen(
                    onRetry = onRetry,
                    modifier = Modifier.padding(paddingValues),
                    message = "Error al obtener la ubicación.\nIntenta de nuevo"
                )
            }

            uiState.isLoading -> {
                LoadingScreen(
                    onClick = onLoadingClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            uiState.data != null -> {
                val location = uiState.data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier.height(40.dp)
                    )

                    Text(
                        text = location.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(
                        modifier = Modifier.height(40.dp)
                    )

                    LocationDetailsInfoRow(
                        label = "ID:",
                        value = location.id.toString()
                    )

                    LocationDetailsInfoRow(
                        label = "Type:",
                        value = location.type
                    )

                    LocationDetailsInfoRow(
                        label = "Dimension:",
                        value = location.dimension
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationDetailsInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LocationDetailsLoadingPreview() {
    Lab7Theme {
        LocationDetailsContent(
            uiState = LocationDetailsUiState(
                isLoading = true
            ),
            onBack = {},
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
fun LocationDetailsErrorPreview() {
    Lab7Theme {
        LocationDetailsContent(
            uiState = LocationDetailsUiState(
                isLoading = false,
                hasError = true
            ),
            onBack = {},
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
fun LocationDetailsDataPreview() {
    Lab7Theme {
        LocationDetailsContent(
            uiState = LocationDetailsUiState(
                isLoading = false,
                data = LocationDb().getLocationById(1)
            ),
            onBack = {},
            onLoadingClick = {},
            onRetry = {}
        )
    }
}
