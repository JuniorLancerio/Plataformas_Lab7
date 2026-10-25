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
import kotlinx.coroutines.launch
import plat.lab7.data.Location
import plat.lab7.data.RickAndMortyRepository
import plat.lab7.data.local.AppDatabase

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

class LocationsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RickAndMortyRepository(
        AppDatabase.getDatabase(application)
    )

    private val _uiState = MutableStateFlow(LocationsUiState())

    val uiState: StateFlow<LocationsUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocations()
    }

    fun loadLocations() {
        loadingJob?.cancel()

        _uiState.value = LocationsUiState(
            isLoading = true,
            data = emptyList(),
            hasError = false
        )

        loadingJob = viewModelScope.launch {
            try {
                delay(4000)

                repository.getAllLocations().collect { locations ->
                    _uiState.value = LocationsUiState(
                        isLoading = false,
                        data = locations,
                        hasError = false
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = LocationsUiState(
                    isLoading = false,
                    data = emptyList(),
                    hasError = true
                )
            }
        }
    }

    fun showError() {
        if (_uiState.value.isLoading) {
            loadingJob?.cancel()

            _uiState.value = LocationsUiState(
                isLoading = false,
                data = emptyList(),
                hasError = true
            )
        }
    }
}
