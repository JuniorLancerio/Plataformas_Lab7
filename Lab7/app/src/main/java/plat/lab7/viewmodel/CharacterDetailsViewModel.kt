package plat.lab7.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab7.data.Character
import plat.lab7.data.CharacterDb
import plat.lab7.navigation.CharacterDetailsDestination

data class CharacterDetailsUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

class CharacterDetailsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterDb = CharacterDb()

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

        _uiState.value = CharacterDetailsUiState(
            isLoading = true,
            data = null,
            hasError = false
        )

        loadingJob = viewModelScope.launch {
            delay(2000)

            try {
                val character = characterDb.getCharacterById(characterId)

                _uiState.value = CharacterDetailsUiState(
                    isLoading = false,
                    data = character,
                    hasError = false
                )
            } catch (e: Exception) {
                _uiState.value = CharacterDetailsUiState(
                    isLoading = false,
                    data = null,
                    hasError = true
                )
            }
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            loadingJob?.cancel()

            _uiState.value = CharacterDetailsUiState(
                isLoading = false,
                data = null,
                hasError = true
            )
        }
    }
}
