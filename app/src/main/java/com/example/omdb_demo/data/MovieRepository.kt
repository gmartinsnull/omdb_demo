package com.example.omdb_demo.data

import com.example.omdb_demo.data.local.Movie
import com.example.omdb_demo.data.local.MovieDao
import com.example.omdb_demo.data.remote.ApiResult
import com.example.omdb_demo.data.remote.MovieNotFoundThrowable
import com.example.omdb_demo.data.remote.RemoteDataSource
import com.example.omdb_demo.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import networkBoundResource
import javax.inject.Inject

/**
 *  repository responsible for handling movie data from the network and storing it in the local database
 *  accordingly
 */
class MovieRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val movieDao: MovieDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * attempts to retrieve movie data from local database. Otherwise, fetches from API
     */
    fun getMovieByTitle(title: String): Flow<ApiResult<List<Movie>>> {
        return networkBoundResource(
            query = {
                movieDao.getMovieByTitle(title)
            },
            fetch = {
                remoteDataSource.fetchMovieByTitle(title)
            },
            saveFetchResult = { response ->
                val data = response.first().data
                if (data == null || data.search.isNullOrEmpty() && data.error != null) { // api may return success but still contain error in body
                    throw MovieNotFoundThrowable(data?.error ?: "error while fetching movie by title: $title")
                } else {
                    data.search?.forEach {
                        movieDao.insertAll(it)
                    }
                }
            }
        ).flowOn(ioDispatcher)
    }

    /**
     * retrieves list of all movies from the database
     */
    fun getMovies(): Flow<ApiResult<List<Movie>>> = flow {
        val data = movieDao.getAll().first()
        emit(ApiResult.success(data))
    }
}