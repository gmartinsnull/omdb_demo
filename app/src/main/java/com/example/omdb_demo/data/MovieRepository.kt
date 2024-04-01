package com.example.omdb_demo.data

import com.example.omdb_demo.data.local.Movie
import com.example.omdb_demo.data.remote.ApiResult
import kotlinx.coroutines.flow.Flow

/**
 *  abstraction of base movie repository implementation
 */
abstract class MovieRepository {
    /**
     * attempts to retrieve movie data from local database. Otherwise, fetches from API
     */
    abstract fun getMovies(title: String, offset: Int): Flow<ApiResult<List<Movie>>>

    /**
     * attempts to retrieve movie data from API endpoint. If successful, stores in local database
     */
    abstract fun fetchMovies(title: String, offset: Int): Flow<ApiResult<List<Movie>>>
}