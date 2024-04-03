package com.example.omdb_demo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb_demo.data.remote.Status
import com.example.omdb_demo.domain.GetMovieByTitle
import com.example.omdb_demo.domain.GetMovies
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
    private val getMovies: GetMovies,
    private val getMovieByTitle: GetMovieByTitle
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    /**
     * fetching data from repository and parsing it accordingly for view layer consumption
     */
    fun fetchData(title: String) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            if (title.isEmpty()) {
                getAllMovies()
            } else {
                getMoviesByTitle(title)
            }
        }
    }

    /**
     * get all movies stored locally
     */
    private suspend fun getAllMovies() {
        getMovies().collect { result ->
            _state.value = when (result.status) {
                Status.SUCCESS -> UiState.Loaded(result.data ?: emptyList())
                Status.ERROR -> UiState.Error(
                    result.error?.message ?: "error while retrieving all movies"
                )
            }
        }
    }

    /**
     * get all movies either stored locally or from API
     */
    private suspend fun getMoviesByTitle(title: String) {
        getMovieByTitle(title).collect { result ->
            _state.value = when (result.status) {
                Status.SUCCESS -> UiState.Loaded(result.data ?: emptyList())
                Status.ERROR -> UiState.Error(
                    result.error?.message ?: "error while retrieving movie by title"
                )
            }
        }
    }
}