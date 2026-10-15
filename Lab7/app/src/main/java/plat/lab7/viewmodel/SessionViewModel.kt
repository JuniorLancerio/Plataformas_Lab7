
package plat.lab7.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab7.data.UserPreferencesRepository

data class SessionUiState(
    val isLoading: Boolean = true,
    val userName: String? = null
)

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)

    private val _uiState = MutableStateFlow(SessionUiState())

    val uiState: StateFlow<SessionUiState> =
        _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.userName.collect { name ->
                _uiState.value = SessionUiState(
                    isLoading = false,
                    userName = name?.takeIf { it.isNotBlank() }
                )
            }
        }
    }

    fun login(
        name: String,
        onSuccess: () -> Unit
    ) {
        val validName = name.trim()

        if (validName.isEmpty()) {
            return
        }

        viewModelScope.launch {
            repository.saveName(validName)
            onSuccess()
        }
    }

    fun logout(
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.clearName()
            onSuccess()
        }
    }
}
