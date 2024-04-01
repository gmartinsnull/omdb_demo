package com.example.omdb_demo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 *  data class representing movie local data
 */
@Entity
data class Movie(
    @PrimaryKey
    val title: String,
    val year: Int,
    val poster: String
)