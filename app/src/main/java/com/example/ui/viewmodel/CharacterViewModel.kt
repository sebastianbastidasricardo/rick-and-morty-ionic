package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CharacterDto
import com.example.data.repository.CharacterRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CharacterUiState(
    val characters: List<CharacterDto> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedStatus: String? = null, // null = all, "alive", "dead", "unknown"
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,
    val selectedCharacter: CharacterDto? = null
)

class CharacterViewModel(
    private val repository: CharacterRepository = CharacterRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterUiState())
    val uiState: StateFlow<CharacterUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadCharacters(reset = true)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350)
            loadCharacters(reset = true)
        }
    }

    fun onStatusSelected(status: String?) {
        if (_uiState.value.selectedStatus == status) return
        _uiState.update { it.copy(selectedStatus = status) }
        loadCharacters(reset = true)
    }

    fun loadCharacters(reset: Boolean = false) {
        val currentState = _uiState.value
        val targetPage = if (reset) 1 else currentState.currentPage + 1

        if (!reset && (!currentState.hasMorePages || currentState.isLoadingMore || currentState.isLoading)) {
            return
        }

        viewModelScope.launch {
            if (reset) {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null,
                        currentPage = 1
                    )
                }
            } else {
                _uiState.update { it.copy(isLoadingMore = true) }
            }

            val result = repository.getCharacters(
                page = targetPage,
                name = currentState.searchQuery,
                status = currentState.selectedStatus
            )

            result.fold(
                onSuccess = { response ->
                    val newCharacters = if (reset) {
                        response.results
                    } else {
                        currentState.characters + response.results
                    }
                    _uiState.update {
                        it.copy(
                            characters = newCharacters,
                            isLoading = false,
                            isLoadingMore = false,
                            errorMessage = null,
                            currentPage = targetPage,
                            hasMorePages = response.info.next != null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            errorMessage = if (reset && it.characters.isEmpty()) {
                                error.localizedMessage ?: "Error de conexión"
                            } else null
                        )
                    }
                }
            )
        }
    }

    fun selectCharacter(character: CharacterDto?) {
        _uiState.update { it.copy(selectedCharacter = character) }
    }
}
