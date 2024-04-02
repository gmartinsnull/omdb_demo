package com.example.omdb_demo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb_demo.data.remote.Status
import com.example.omdb_demo.domain.GetMovieByTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *  viewmodel class responsible for handling data from data to view layer
 */
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMovieByTitle: GetMovieByTitle
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    /**
     * fetching data from repository and parsing it accordingly for the view layer
     */
    fun fetchData(title: String) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            getMovieByTitle(title).collect { result ->
                _state.value = when (result.status) {
                    Status.SUCCESS -> UiState.Loaded(result.data ?: emptyList())
                    Status.ERROR -> {
                        if (result.error?.code == 301) {
                            UiState.Empty
                        } else {
                            UiState.Error(
                                result.error?.message ?: "error while retrieving movies"
                            )
                        }
                    }
                }
            }
        }
    }
}