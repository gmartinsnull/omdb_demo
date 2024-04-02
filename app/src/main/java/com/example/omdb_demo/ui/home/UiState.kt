package com.example.omdb_demo.ui.home

import com.example.omdb_demo.data.local.Movie

/**
 *  state class representing the current/latest UI state
 */
sealed class UiState {
    data object Loading : UiState()
    data class Loaded(val data: List<Movie>) : UiState()
    data object Empty : UiState()
    data class Error(val errorMessage: String) : UiState()
}