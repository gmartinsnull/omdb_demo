package com.example.omdb_demo.data.remote

import com.example.omdb_demo.data.local.Movie
import com.squareup.moshi.Json

/**
 *  data class representing movie API response object
 */
data class MovieResponse(
    @Json(name = "Search")
    val search: List<Movie>?,
    @Json(name = "Response")
    val response: String?,
    @Json(name = "Error")
    val error: String?
)