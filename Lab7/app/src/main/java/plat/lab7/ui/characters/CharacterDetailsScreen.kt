package plat.lab7.ui.characters

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import plat.lab7.data.CharacterDb
import plat.lab7.ui.components.CharacterImage
import plat.lab7.ui.components.ErrorScreen
import plat.lab7.ui.components.LoadingScreen
import plat.lab7.ui.theme.Lab7Theme
import plat.lab7.viewmodel.CharacterDetailsUiState
import plat.lab7.viewmodel.CharacterDetailsViewModel

@Composable
fun CharacterDetailsScreen(
    onBack: () -> Unit,
    characterDetailsViewModel: CharacterDetailsViewModel = viewModel()
) {
    val uiState by characterDetailsViewModel.uiState.collectAsStateWithLifecycle()

    CharacterDetailsContent(
        uiState = uiState,
        onBack = onBack,
        onFavoriteClick = characterDetailsViewModel::toggleFavorite,
        onLoadingClick = characterDetailsViewModel::showError,
        onRetry = characterDetailsViewModel::loadCharacter
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsContent(
    uiState: CharacterDetailsUiState,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Characters details")
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
                    message = "Error al obtener el personaje.\nIntenta de nuevo"
                )
            }

            uiState.isLoading -> {
                LoadingScreen(
                    onClick = onLoadingClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            uiState.data != null -> {
                val character = uiState.data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier.height(32.dp)
                    )

                    CharacterImage(
                        image = character.image,
                        name = character.name,
                        size = 220.dp
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = onFavoriteClick
                        ) {
                            Icon(
                                imageVector = if (character.isFavorite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Outlined.FavoriteBorder
                                },
                                contentDescription = if (character.isFavorite) {
                                    "Quitar de favoritos"
                                } else {
                                    "Agregar a favoritos"
                                },
                                tint = if (character.isFavorite) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(40.dp)
                    )

                    CharacterDetailsInfoRow(
                        label = "Species:",
                        value = character.species
                    )

                    CharacterDetailsInfoRow(
                        label = "Status:",
                        value = character.status
                    )

                    CharacterDetailsInfoRow(
                        label = "Gender:",
                        value = character.gender
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailsInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharacterDetailsLoadingPreview() {
    Lab7Theme {
        CharacterDetailsContent(
            uiState = CharacterDetailsUiState(
                isLoading = true
            ),
            onBack = {},
            onFavoriteClick = {},
            onLoadingClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharacterDetailsErrorPreview() {
    Lab7Theme {
        CharacterDetailsContent(
            uiState = CharacterDetailsUiState(
                isLoading = false,
                hasError = true
            ),
            onBack = {},
            onFavoriteClick = {},
            onLoadingClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharacterDetailsDataPreview() {
    Lab7Theme {
        CharacterDetailsContent(
            uiState = CharacterDetailsUiState(
                isLoading = false,
                data = CharacterDb().getCharacterById(1)
            ),
            onBack = {},
            onFavoriteClick = {},
            onLoadingClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharacterDetailsFavoritePreview() {
    Lab7Theme {
        CharacterDetailsContent(
            uiState = CharacterDetailsUiState(
                isLoading = false,
                data = CharacterDb()
                    .getCharacterById(1)
                    .copy(isFavorite = true)
            ),
            onBack = {},
            onFavoriteClick = {},
            onLoadingClick = {},
            onRetry = {}
        )
    }
}
