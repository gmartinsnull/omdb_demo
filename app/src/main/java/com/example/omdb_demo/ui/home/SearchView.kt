package com.example.omdb_demo.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *  composable class responsible for displaying a search bar
 */
@Composable
fun SearchViewComponent(
    modifier: Modifier,
    onSearch: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    SearchView(
        modifier = modifier.widthIn(0.dp,800.dp),
        query = searchQuery,
        onSearching = {
            searchQuery = it
        },
        onSearch = {
            onSearch.invoke(it)
        },
        keyboardController = keyboardController
    )
}

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    query: String,
    onSearch: (String) -> Unit,
    onSearching: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?
) {
    Box(
        modifier = modifier
            .padding(top = 15.dp, start = 5.dp, end = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
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

@Preview(showBackground = true)
@Composable
fun SearchViewPreview() {
    SearchView(
        query = "Batman",
        onSearch = {},
        onSearching = {},
        keyboardController = null
    )
}