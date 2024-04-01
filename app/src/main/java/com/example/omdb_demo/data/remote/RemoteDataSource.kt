package com.example.omdb_demo.data.remote

import com.example.omdb_demo.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 *  data source class responsible for fetching data from API endpoint
 */
class RemoteDataSource @Inject constructor(
    private val service: MovieService
) {
    /**
     * fetches data from movie API endpoint
     */
    fun fetchMovieByTitle(title: String): Flow<ApiResult<MovieResponse>> = flow {
        val apiKey = BuildConfig.API_KEY
        emit(handleApiCall { service.getMovieByTitle(apiKey, title) })
    }
}