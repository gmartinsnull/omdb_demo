package com.example.omdb_demo.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omdb_demo.ErrorMessageScreen
import com.example.omdb_demo.data.local.Movie
import com.example.omdb_demo.ui.theme.MainTheme

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
                .padding(innerPadding),
            state = state.value,
            onClick = {
                Toast.makeText(context, "${it.title} clicked", Toast.LENGTH_LONG).show()
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
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchViewComponent(
            modifier = modifier,
            onSearch = { onSearch.invoke(it) }
        )
        when (state) {
            is UiState.Loading -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Loaded -> {
                if (state.data.isNotEmpty()) {
                    LazyVerticalGrid(
                        modifier = modifier
                            .widthIn(0.dp,800.dp)
                            .padding(
                                top = 10.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 10.dp
                            ),
                        columns = GridCells.Adaptive(120.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.data) { movie ->
                            MovieItemScreen(
                                movieItem = movie,
                                onClick = { onClick.invoke(movie) }
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = modifier.fillMaxSize(),
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
@PreviewScreenSizes
@PreviewLightDark
@Composable
fun MoviesHomePreview() {
    MainTheme {
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
            onSearch = {},
            onRefresh = {

            },
            onClick = {

            }
        )
    }
}