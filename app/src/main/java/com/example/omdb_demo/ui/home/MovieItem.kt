package com.example.omdb_demo.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.omdb_demo.R
import com.example.omdb_demo.data.local.Movie
import com.example.omdb_demo.ui.theme.MainTheme

/**
 *  composable class responsible for displaying a movie item
 */
@Composable
fun MovieItemScreen(
    movieItem: Movie,
    onClick: (Movie) -> Unit
) {
    MovieItem(movieItem) {
        onClick.invoke(movieItem)
    }
}

@Composable
fun MovieItem(
    item: Movie,
    onClick: (Movie) -> Unit
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(250.dp)
            .clip(shape = RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.tertiary)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberAsyncImagePainter(
                model = item.poster,
                error = painterResource(id = R.drawable.poster_not_found),
                placeholder = painterResource(id = R.drawable.movie_poster_placeholder)
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .padding(top = 14.dp, start = 10.dp)
        ) {
            Text(
                modifier = Modifier.width(200.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                text = item.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = item.year,
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Button(
            modifier = Modifier
                .width(120.dp)
                .padding(bottom = 5.dp)
                .align(Alignment.BottomCenter),
            onClick = { onClick.invoke(item) }
        ) {
            Text(
                color = Color.White,
                text = stringResource(id = R.string.card_btn_text)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    MainTheme {
        MovieItem(
            item = Movie(
                "Jurassic Park",
                "1993",
                "https://m.media-amazon.com/images/M/MV5BMzRmY2EzODUtMWNiMi00MWJlLWFjMGYtY2Y0YWIwOTNkZmY2XkEyXkFqcGdeQXVyNjExODE1MDc@._V1_SX300.jpg"
            )
        ) {

        }
    }
}