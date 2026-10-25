package plat.lab7.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.lab7.data.Character
import plat.lab7.data.RickAndMortyRepository
import plat.lab7.data.local.AppDatabase

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false,
    val showOnlyFavorites: Boolean = false
)

class CharactersViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RickAndMortyRepository(
        AppDatabase.getDatabase(application)
    )

    private val _showOnlyFavorites = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(CharactersUiState())

    val uiState: StateFlow<CharactersUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        loadingJob?.cancel()

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            hasError = false
        )

        loadingJob = viewModelScope.launch {
            try {
                delay(4000)

                combine(
                    repository.getAllCharacters(),
                    _showOnlyFavorites
                ) { characters, onlyFavorites ->
                    if (onlyFavorites) {
                        characters.filter { it.isFavorite }
                    } else {
                        characters
                    }
                }.collect { characters ->
                    _uiState.value = CharactersUiState(
                        isLoading = false,
                        data = characters,
                        hasError = false,
                        showOnlyFavorites = _showOnlyFavorites.value
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasError = true
                )
            }
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            loadingJob?.cancel()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasError = true
            )
        }
    }

    fun setShowOnlyFavorites(showOnlyFavorites: Boolean) {
        _showOnlyFavorites.value = showOnlyFavorites
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            repository.updateFavorite(
                id = character.id,
                isFavorite = !character.isFavorite
            )
        }
    }
}
