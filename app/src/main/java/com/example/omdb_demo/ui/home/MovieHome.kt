package com.example.omdb_demo.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
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
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    vm.fetchData("")

    Scaffold(
        topBar = { }
    ) { innerPadding ->
        MovieHome(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            state = state.value,
            onClick = {
                Toast.makeText(context, "movie title: ${it.title}", Toast.LENGTH_LONG).show()
            },
            query = searchQuery,
            onSearch = {
                vm.fetchData(it)
            },
            onSearching = {
                searchQuery = it
            },
            onRefresh = {
                vm.fetchData("")
            },
            keyboardController = keyboardController
        )
    }
}

@Composable
fun MovieHome(
    modifier: Modifier = Modifier,
    state: UiState,
    onClick: (Movie) -> Unit,
    query: String,
    onSearching: (String) -> Unit,
    onSearch: (String) -> Unit,
    onRefresh: () -> Unit,
    keyboardController: SoftwareKeyboardController?
) {
    when (state) {
        is UiState.Loading -> {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 5.dp, end = 5.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.TopCenter
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = query,
                        onValueChange = {
                            onSearching.invoke(it)
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = "")
                        },
                        placeholder = { Text(text = "Search movie by title") },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                                onSearch.invoke(query)
                            }
                        )
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Empty -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, start = 5.dp, end = 5.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = query,
                    onValueChange = {
                        onSearching.invoke(it)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = "")
                    },
                    placeholder = { Text(text = "Search movie by title") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            onSearch.invoke(query)
                        }
                    )
                )
            }
        }

        is UiState.Loaded -> {
            if (state.data.isEmpty()) {
                ErrorMessageScreen(
                    message = "Darn! No movies were found this time.",
                    buttonEnabled = true
                ) {
                    onRefresh.invoke()
                }
            } else {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, start = 5.dp, end = 5.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = query,
                            onValueChange = {
                                onSearching.invoke(it)
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Rounded.Search, contentDescription = "")
                            },
                            placeholder = { Text(text = "Search movie by title") },
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    keyboardController?.hide()
                                    onSearch.invoke(query)
                                }
                            )
                        )
                    }
                    LazyVerticalGrid(
                        modifier = modifier.padding(top = 10.dp),
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                )
            )
        ),
        onClick = {},
        onSearch = {},
        query = "Dune",
        onSearching = {

        },
        keyboardController = null,
        onRefresh = {

        }
    )
}