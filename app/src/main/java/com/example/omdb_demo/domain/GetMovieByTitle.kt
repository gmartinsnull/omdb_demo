package com.example.omdb_demo.domain

import com.example.omdb_demo.data.MovieRepository
import com.example.omdb_demo.data.local.Movie
import com.example.omdb_demo.data.remote.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *  usecase class responsible for isolating logic for get movie by title event
 */
class GetMovieByTitle @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(title: String): Flow<ApiResult<List<Movie>>> {
        return repository.getMovieByTitle(title)
    }
}