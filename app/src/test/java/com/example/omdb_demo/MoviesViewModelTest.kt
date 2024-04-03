package com.example.omdb_demo

import com.example.omdb_demo.data.local.Movie
import com.example.omdb_demo.data.remote.ApiError
import com.example.omdb_demo.data.remote.ApiResult
import com.example.omdb_demo.data.remote.Status
import com.example.omdb_demo.domain.GetMovieByTitle
import com.example.omdb_demo.domain.GetMovies
import com.example.omdb_demo.ui.home.MoviesViewModel
import com.example.omdb_demo.ui.home.UiState
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

/**
 * unit test class for movies home viewmodel
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MoviesHomeViewModelTest {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)

    private lateinit var viewModel: MoviesViewModel

    @Mock
    private lateinit var getMovieByTitle: GetMovieByTitle

    @Mock
    private lateinit var getMovies: GetMovies

    @Before
    fun init() {
        viewModel = MoviesViewModel(getMovies, getMovieByTitle)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ui state  - get movie by title - success`() = runTest {
        val fakeGetMovieByTitle = FakeGetMovieByTitle<List<Movie>>()

        `when`(getMovieByTitle("Jurassic Park")).thenReturn(fakeGetMovieByTitle.flow)

        viewModel.fetchData("Jurassic Park")

        val state = mutableListOf<UiState>()

        backgroundScope.launch(testDispatcher) {
            viewModel.state.onEach(state::add).collect()
        }

        val response = ApiResult.success(genMovies())
        fakeGetMovieByTitle.emit(response)


        assertTrue(state.size == 2)
        assertThat(state, instanceOf(ArrayList::class.java))
        assertTrue((state[1] as UiState.Loaded).data.size == 3)
        assertTrue((state[1] as UiState.Loaded).data[1].title == "The Lost World: Jurassic Park")
    }

    @Test
    fun `ui state - get movie by title - error`() = runTest {
        val fakeGetMovieByTitle = FakeGetMovieByTitle<List<Movie>>()

        `when`(getMovieByTitle("zaaaaa")).thenReturn(fakeGetMovieByTitle.flow)

        viewModel.fetchData("zaaaaa")

        val state = mutableListOf<UiState>()

        backgroundScope.launch(testDispatcher) {
            viewModel.state.onEach(state::add).collect()
        }

        val response = ApiResult(
            status = Status.ERROR,
            data = emptyList<Movie>(),
            error = ApiError(404, "movie not found")
        )
        fakeGetMovieByTitle.emit(response)

        assertTrue(state.size == 2)
        assertThat(state[1], instanceOf(UiState.Error::class.java))
        assertTrue((state[1] as UiState.Error).errorMessage == "movie not found")
    }

    @Test
    fun `ui state  - get movies - success`() = runTest {
        val fakeGetMovieByTitle = FakeGetMovieByTitle<List<Movie>>()

        `when`(getMovies()).thenReturn(fakeGetMovieByTitle.flow)

        viewModel.fetchData("")

        val state = mutableListOf<UiState>()

        backgroundScope.launch(testDispatcher) {
            viewModel.state.onEach(state::add).collect()
        }

        val response = ApiResult.success(genMovies())
        fakeGetMovieByTitle.emit(response)


        assertTrue(state.size == 2)
        assertThat(state, instanceOf(ArrayList::class.java))
        assertTrue((state[1] as UiState.Loaded).data.size == 3)
        assertTrue((state[1] as UiState.Loaded).data[1].title == "The Lost World: Jurassic Park")
    }

    @Test
    fun `ui state - get movies - error`() = runTest {
        val fakeGetMovieByTitle = FakeGetMovieByTitle<List<Movie>>()

        `when`(getMovies()).thenReturn(fakeGetMovieByTitle.flow)

        viewModel.fetchData("")

        val state = mutableListOf<UiState>()

        backgroundScope.launch(testDispatcher) {
            viewModel.state.onEach(state::add).collect()
        }

        val response = ApiResult(
            status = Status.ERROR,
            data = emptyList<Movie>(),
            error = ApiError(500, "api error")
        )
        fakeGetMovieByTitle.emit(response)

        assertTrue(state.size == 2)
        assertThat(state[1], instanceOf(UiState.Error::class.java))
        assertTrue((state[1] as UiState.Error).errorMessage == "api error")
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
            )
        )
}

/**
 * test class required in order to test StateFlow variable in viewmodel
 */
class FakeGetMovieByTitle<T> {
    val flow = MutableSharedFlow<ApiResult<T>>()

    suspend fun emit(value: ApiResult<T>) {
        flow.emit(value)
    }
}