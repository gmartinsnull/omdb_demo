package com.example.omdb_demo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 *  class representing application database
 */
@Database([Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
}