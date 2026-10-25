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
import plat.lab7.data.Location
import plat.lab7.data.RickAndMortyRepository
import plat.lab7.data.local.AppDatabase
import plat.lab7.navigation.LocationDetailsDestination

data class LocationDetailsUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class LocationDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository = RickAndMortyRepository(
        AppDatabase.getDatabase(application)
    )

    private val locationId: Int =
        savedStateHandle.toRoute<LocationDetailsDestination>().id

    private val _uiState = MutableStateFlow(LocationDetailsUiState())

    val uiState: StateFlow<LocationDetailsUiState> =
        _uiState.asStateFlow()

    private var loadingJob: Job? = null

    init {
        loadLocation()
    }

    fun loadLocation() {
        loadingJob?.cancel()

        _uiState.value = LocationDetailsUiState(
            isLoading = true,
            data = null,
            hasError = false
        )

        loadingJob = viewModelScope.launch {
            try {
                delay(2000)

                repository.getLocationById(locationId).collect { location ->
                    _uiState.value = LocationDetailsUiState(
                        isLoading = false,
                        data = location,
                        hasError = location == null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = LocationDetailsUiState(
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

            _uiState.value = LocationDetailsUiState(
                isLoading = false,
                data = null,
                hasError = true
            )
        }
    }
}
