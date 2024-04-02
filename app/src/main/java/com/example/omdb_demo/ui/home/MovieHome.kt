package com.example.omdb_demo.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omdb_demo.ErrorMessageScreen
import com.example.omdb_demo.data.local.Movie

/**
 *  composable class responsible for displaying a list of movies
 */
@Composable
fun MoviesHomeScreen() {
    val context = LocalContext.current
    val vm: MoviesViewModel = viewModel()
    val state = vm.state.collectAsState()

    vm.fetchData("")

    Scaffold(
        topBar = { }
    ) { innerPadding ->
        MovieHome(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = state.value,
            onClick = {
                Toast.makeText(context, "movie title: ${it.title}", Toast.LENGTH_LONG).show()
            },
            onSearch = {
                vm.fetchData(it)
            },
            onRefresh = {
                vm.fetchData("")
            }
        )
    }
}

@Composable
fun MovieHome(
    modifier: Modifier = Modifier,
    state: UiState,
    onClick: (Movie) -> Unit,
    onSearch: (String) -> Unit,
    onRefresh: () -> Unit
) {
    Column {
        SearchViewComponent(onSearch = { onSearch.invoke(it) })
        when (state) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Loaded -> {
                if (state.data.isNotEmpty()) {
                    Column {
                        LazyVerticalGrid(
                            modifier = modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp),
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.data) { movie ->
                                MovieItemScreen(
                                    movieItem = movie,
                                    onClick = onClick
                                )
                            }
                        }
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorMessageScreen(
                        message = state.errorMessage,
                        buttonEnabled = true
                    ) {
                        onRefresh.invoke()
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun MoviesHomePreview() {
    MovieHome(
        state = UiState.Loaded(
            listOf(
                Movie(
                    "Jurassic Park",
                    "1993",
                    "https://m.media-amazon.com/images/M/MV5BMzRmY2EzODUtMWNiMi00MWJlLWFjMGYtY2Y0YWIwOTNkZmY2XkEyXkFqcGdeQXVyNjExODE1MDc@._V1_SX300.jpg"
                ),
                Movie(
                    "Jurassic Park",
                    "1993",
                    "https://m.media-amazon.com/images/M/MV5BMzRmY2EzODUtMWNiMi00MWJlLWFjMGYtY2Y0YWIwOTNkZmY2XkEyXkFqcGdeQXVyNjExODE1MDc@._V1_SX300.jpg"
                ),
                Movie(
                    "Jurassic Park",
                    "1993",
                    "https://m.media-amazon.com/images/M/MV5BMzRmY2EzODUtMWNiMi00MWJlLWFjMGYtY2Y0YWIwOTNkZmY2XkEyXkFqcGdeQXVyNjExODE1MDc@._V1_SX300.jpg"
                ),
                Movie(
                    "Jurassic Park",
                    "1993",
                    "https://m.media-amazon.com/images/M/MV5BMzRmY2EzODUtMWNiMi00MWJlLWFjMGYtY2Y0YWIwOTNkZmY2XkEyXkFqcGdeQXVyNjExODE1MDc@._V1_SX300.jpg"
                ),
                Movie(
                    "Jurassic Park",
                    "1993",
                    "https://m.media-amazon.com/images/M/MV5BMzRmY2EzODUtMWNiMi00MWJlLWFjMGYtY2Y0YWIwOTNkZmY2XkEyXkFqcGdeQXVyNjExODE1MDc@._V1_SX300.jpg"
                ),
                Movie(
                    "Jurassic Park",
                    "1993",
                    "https://m.media-amazon.com/images/M/MV5BMzRmY2EzODUtMWNiMi00MWJlLWFjMGYtY2Y0YWIwOTNkZmY2XkEyXkFqcGdeQXVyNjExODE1MDc@._V1_SX300.jpg"
                ),
                Movie(
                    "Jurassic Park",
                    "1993",
                    "https://m.media-amazon.com/images/M/MV5BMzRmY2EzODUtMWNiMi00MWJlLWFjMGYtY2Y0YWIwOTNkZmY2XkEyXkFqcGdeQXVyNjExODE1MDc@._V1_SX300.jpg"
                ),
            )
        ),
        onClick = {},
        onSearch = {},
        onRefresh = {

        }
    )
}