package com.example.omdb_demo.domain

import com.example.omdb_demo.data.MovieRepository
import com.example.omdb_demo.data.local.Movie
import com.example.omdb_demo.data.remote.ApiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *  usecase class responsible for isolating logic to get all movies from the database
 */
class GetMovies @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(): Flow<ApiResult<List<Movie>>> {
        return repository.getMovies()
    }
}