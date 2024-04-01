package com.example.omdb_demo.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  service class responsible for handling movies API calls
 */
interface MovieService {
    @GET("/")
    suspend fun getMovieByTitle(@Query("apikey") apiKey: String, @Query("s") title: String): Response<MovieResponse>
}