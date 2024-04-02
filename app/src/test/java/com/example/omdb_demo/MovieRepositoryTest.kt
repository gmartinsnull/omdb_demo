package com.example.omdb_demo

import com.example.omdb_demo.data.MovieRepositoryImpl
import com.example.omdb_demo.data.local.Movie
import com.example.omdb_demo.data.local.MovieDao
import com.example.omdb_demo.data.remote.ApiError
import com.example.omdb_demo.data.remote.ApiResult
import com.example.omdb_demo.data.remote.MovieResponse
import com.example.omdb_demo.data.remote.RemoteDataSource
import com.example.omdb_demo.data.remote.Status
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * unit test class for movie repository
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private lateinit var repository: MovieRepositoryImpl

    @Mock
    private lateinit var remoteDataSource: RemoteDataSource

    @Mock
    private lateinit var dao: MovieDao

    @Before
    fun setup() {
        repository = MovieRepositoryImpl(remoteDataSource, dao, testDispatcher)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch movie network - success`() = runTest {
        val response = genMovies()
        val movieTitle = "Jurassic Park"
        `when`(remoteDataSource.fetchMovieByTitle(movieTitle)).thenReturn(
            flowOf(
                ApiResult.success(
                    MovieResponse(
                        response,
                        null,
                        null
                    )
                )
            )
        )
        `when`(dao.getMovieByTitle(movieTitle)).thenReturn(flowOf(emptyList()), flowOf(response))

        val result = repository.getMovies(movieTitle).toList()

        verify(remoteDataSource).fetchMovieByTitle(movieTitle)
        verify(dao, Mockito.times(2)).getMovieByTitle(movieTitle)

        assertTrue(result[0].data?.size == 3)
    }

    @Test
    fun `fetch movies network - error`() = runTest {
        val movieTitle = "zaaaaa"
        `when`(remoteDataSource.fetchMovieByTitle(movieTitle)).thenReturn(
            flowOf(
                ApiResult.error(
                    ApiError(
                        404,
                        "movie not found"
                    )
                )
            )
        )
        `when`(dao.getMovieByTitle(movieTitle)).thenReturn(flowOf(emptyList()))

        val result = repository.getMovies(movieTitle).toList()

        verify(remoteDataSource).fetchMovieByTitle(movieTitle)
        verify(dao).getMovieByTitle(movieTitle)

        assertTrue(result[0].status == Status.ERROR)
        assertTrue(result[0].error?.code == 404)
    }

    @Test
    fun `fetch movie network - success - empty title - database all`() = runTest {
        val response = genMovies()
        val movieTitle = ""
        `when`(dao.getAll()).thenReturn(flowOf(response))

        val result = repository.getMovies(movieTitle).toList()

        verify(dao).getAll()

        assertTrue(result[0].data?.size == 3)
    }

    @Test
    fun `fetch movies network - success - not found`() = runTest {
        val movieTitle = "zaaaaa"
        val response = MovieResponse(
            null,
            "False",
            "Movie not found!"
        )
        `when`(remoteDataSource.fetchMovieByTitle(movieTitle)).thenReturn(
            flowOf(ApiResult.success(response))
        )
        `when`(dao.getMovieByTitle(movieTitle)).thenReturn(flowOf(emptyList()))

        val result = repository.getMovies(movieTitle).toList()

        verify(remoteDataSource).fetchMovieByTitle(movieTitle)
        verify(dao).getMovieByTitle(movieTitle)

        assertTrue(result[0].status == Status.ERROR)
        assertTrue(result[0].data == null)
        assertTrue(result[0].error is ApiError)
        assertTrue((result[0].error as ApiError).code == 404)
        assertTrue((result[0].error as ApiError).message == "Movie not found!")
    }

    /**
     * generate mock data of list of movies
     */
    private fun genMovies() =
        listOf(
            Movie(
                "Jurassic Park",
                "1993",
                ""
            ),
            Movie(
                "The Lost World: Jurassic Park",
                "1997",
                ""
            ),
            Movie(
                "Jurassic Park III",
                "2001",
                ""
            ),
        )
}