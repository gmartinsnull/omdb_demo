package com.example.omdb_demo.data.remote

import com.example.omdb_demo.data.local.Movie

/**
 *  data class representing movie API response object
 */
data class MovieResponse(
    val search: List<Movie>
)