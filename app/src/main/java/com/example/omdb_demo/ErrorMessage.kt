package com.example.omdb_demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 *  compose representing an error screen
 */
@Composable
fun ErrorMessageScreen(message: String, buttonEnabled: Boolean, onTryAgainClick: () -> Unit) {
    ErrorMessage(
        text = message,
        buttonEnabled = buttonEnabled,
        onTryAgainClick = onTryAgainClick
    )
}

@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    text: String,
    buttonEnabled: Boolean,
    onTryAgainClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.clip(shape = RoundedCornerShape(20.dp)),
            painter = painterResource(id = R.drawable.notlikethis),
            contentDescription = "error image"
        )
        Text(
            modifier = modifier,
            text = text,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        if (buttonEnabled)
            Button(
                modifier = Modifier.padding(top = 10.dp),
                onClick = onTryAgainClick
            ) {
                Text(
                    text = "Try again",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorMessagePreview() {
    ErrorMessage(
        text = "Darn! Something went wrong",
        buttonEnabled = true
    ) {

    }
}