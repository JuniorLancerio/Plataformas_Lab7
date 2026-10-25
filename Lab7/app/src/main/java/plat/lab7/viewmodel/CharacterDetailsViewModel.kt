package plat.lab7.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab7.data.Character
import plat.lab7.data.RickAndMortyRepository
import plat.lab7.data.local.AppDatabase
import plat.lab7.navigation.CharacterDetailsDestination

data class CharacterDetailsUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = RickAndMortyRepository(
        AppDatabase.getDatabase(application)
    )

    private val characterId: Int =
        savedStateHandle.toRoute<CharacterDetailsDestination>().id

    private val _uiState = MutableStateFlow(CharacterDetailsUiState())

    val uiState: StateFlow<CharacterDetailsUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        loadingJob?.cancel()

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            hasError = false
        )

        loadingJob = viewModelScope.launch {
            try {
                delay(2000)

                repository.getCharacterById(characterId).collect { character ->
                    _uiState.value = CharacterDetailsUiState(
                        isLoading = false,
                        data = character,
                        hasError = character == null
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

    fun toggleFavorite() {
        val character = _uiState.value.data ?: return

        viewModelScope.launch {
            repository.updateFavorite(
                id = character.id,
                isFavorite = !character.isFavorite
            )
        }
    }
}
