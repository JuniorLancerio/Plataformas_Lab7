package plat.lab7.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.lab7.data.RickAndMortyRepository
import plat.lab7.data.UserPreferencesRepository
import plat.lab7.data.local.AppDatabase

data class SessionUiState(
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val userName: String? = null,
    val hasError: Boolean = false
)

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userRepository = UserPreferencesRepository(application)

    private val repository = RickAndMortyRepository(
        AppDatabase.getDatabase(application)
    )

    private val _uiState = MutableStateFlow(SessionUiState())

    val uiState: StateFlow<SessionUiState> =
        _uiState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            try {
                val savedName = userRepository.userName.first()
                    ?.takeIf { it.isNotBlank() }

                if (savedName != null && !repository.hasInitialData()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true,
                        isSyncing = true
                    )

                    delay(4000)
                    repository.synchronizeInitialData()
                }

                _uiState.value = SessionUiState(
                    isLoading = false,
                    isSyncing = false,
                    userName = savedName,
                    hasError = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = SessionUiState(
                    isLoading = false,
                    isSyncing = false,
                    userName = null,
                    hasError = true
                )
            }
        }
    }

    fun login(
        name: String,
        onSuccess: () -> Unit
    ) {
        val validName = name.trim()

        if (validName.isEmpty() || _uiState.value.isSyncing) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSyncing = true,
                hasError = false
            )

            try {
                if (!repository.hasInitialData()) {
                    delay(4000)
                    repository.synchronizeInitialData()
                }

                userRepository.saveName(validName)

                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    userName = validName
                )

                onSuccess()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    hasError = true
                )
            }
        }
    }

    fun logout(
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSyncing = true,
                hasError = false
            )

            try {
                repository.clearAllData()
                userRepository.clearName()

                _uiState.value = SessionUiState(
                    isLoading = false,
                    isSyncing = false,
                    userName = null,
                    hasError = false
                )

                onSuccess()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    hasError = true
                )
            }
        }
    }
}
