package com.example.omdb_demo.data

import android.util.Log
import com.example.omdb_demo.data.local.Movie
import com.example.omdb_demo.data.local.MovieDao
import com.example.omdb_demo.data.remote.ApiError
import com.example.omdb_demo.data.remote.ApiResult
import com.example.omdb_demo.data.remote.RemoteDataSource
import com.example.omdb_demo.data.remote.Status
import com.example.omdb_demo.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *  repository responsible for handling movie data from the network and storing it in the local database
 *  accordingly
 */
class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val movieDao: MovieDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MovieRepository() {

    override fun getMovies(title: String): Flow<ApiResult<List<Movie>>> = channelFlow {
        try {
            val data: List<Movie> =
                if (title.isNotEmpty()) // retrieves all movies saved in the db, otherwise, returns movies with similar titles
                    movieDao.getMovieByTitle(title).first()
                else
                    movieDao.getAll().first()
            if (data.isEmpty() && title.isEmpty()) {
                Log.i("INFO", "Movie catalog empty")
                send(ApiResult.success(emptyList()))
            } else if (data.isEmpty()) { // checking if data has already been fetched into db
                send(fetchMovies(title).first())
            } else {
                send(ApiResult.success(data))
            }
        } catch (e: Exception) {
            send(ApiResult.error(ApiError(0, e.message ?: "error while retrieving movies")))
        }
    }

    override fun fetchMovies(title: String): Flow<ApiResult<List<Movie>>> {
        return remoteDataSource.fetchMovieByTitle(title).map { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    if (result.data?.error != null) {
                        ApiResult.error(
                            ApiError(
                                result.error?.code ?: 404,
                                result.data.error
                            )
                        )
                    } else {
                        result.data?.search?.forEach {
                            movieDao.insertAll(it)
                        }
                        val storedData = movieDao.getMovieByTitle(title).first()
                        ApiResult.success(storedData)
                    }
                }

                Status.ERROR -> ApiResult.error(
                    ApiError(
                        result.error?.code ?: 0,
                        result.error?.message ?: "unknown api error message"
                    )
                )
            }
        }.flowOn(ioDispatcher)
    }
}