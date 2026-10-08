package plat.lab7.ui.characters

import plat.lab7.ui.components.CharacterImage
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import plat.lab7.data.Character
import plat.lab7.data.CharacterDb
import plat.lab7.ui.components.ErrorScreen
import plat.lab7.ui.components.LoadingScreen
import plat.lab7.ui.theme.Lab7Theme
import plat.lab7.viewmodel.CharactersUiState
import plat.lab7.viewmodel.CharactersViewModel

@Composable
fun CharactersScreen(
    onCharacterClick: (Int) -> Unit,
    charactersViewModel: CharactersViewModel = viewModel()
) {
    val uiState by charactersViewModel.uiState.collectAsStateWithLifecycle()

    CharactersContent(
        uiState = uiState,
        onCharacterClick = onCharacterClick,
        onLoadingClick = charactersViewModel::showError,
        onRetry = charactersViewModel::loadCharacters
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersContent(
    uiState: CharactersUiState,
    onCharacterClick: (Int) -> Unit,
    onLoadingClick: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Characters")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->

        when {
            uiState.hasError -> {
                ErrorScreen(
                    onRetry = onRetry,
                    modifier = Modifier.padding(paddingValues),
                    message = "Error al obtener listado de personajes.\nIntenta de nuevo"
                )
            }

            uiState.isLoading -> {
                LoadingScreen(
                    onClick = onLoadingClick,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(
                        items = uiState.data,
                        key = { it.id }
                    ) { character ->
                        CharacterItem(
                            character = character,
                            onClick = {
                                onCharacterClick(character.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CharacterImage(
                image = character.image,
                name = character.name,
                size = 64.dp
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "${character.species} - ${character.status}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = character.gender,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharactersLoadingPreview() {
    Lab7Theme {
        CharactersContent(
            uiState = CharactersUiState(
                isLoading = true
            ),
            onCharacterClick = {},
            onLoadingClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharactersErrorPreview() {
    Lab7Theme {
        CharactersContent(
            uiState = CharactersUiState(
                isLoading = false,
                hasError = true
            ),
            onCharacterClick = {},
            onLoadingClick = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharactersDataPreview() {
    Lab7Theme {
        CharactersContent(
            uiState = CharactersUiState(
                isLoading = false,
                data = CharacterDb().getAllCharacters()
            ),
            onCharacterClick = {},
            onLoadingClick = {},
            onRetry = {}
        )
    }
}