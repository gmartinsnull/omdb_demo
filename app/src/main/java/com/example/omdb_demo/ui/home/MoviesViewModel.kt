package com.example.omdb_demo.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdb_demo.domain.GetMovieByTitle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *  viewmodel class responsible for handling data from data to view layer
 */
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMovieByTitle: GetMovieByTitle
) : ViewModel() {

    /**
     * fetching data from repository and parsing it accordingly for the view layer
     */
    fun fetchData(title: String) {
        viewModelScope.launch {
            getMovieByTitle(title).collect { result ->
                Log.d("VM", "$result")
            }
        }
    }
}