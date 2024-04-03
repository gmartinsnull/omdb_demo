package com.example.omdb_demo.data.remote

/**
 * custom throwable class for successful response from empty title get movie by title API call
 */
class MovieNotFoundThrowable(message: String) : Throwable(message)