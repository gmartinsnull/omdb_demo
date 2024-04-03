package com.example.omdb_demo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 *  DAO representing all database operations related to movie table
 */
@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movies: Movie)

    @Query("SELECT * FROM movie ORDER BY title, year DESC")
    fun getAll(): Flow<List<Movie>>

    @Query("SELECT * FROM movie WHERE title LIKE '%' || :title || '%'")
    fun getMovieByTitle(title: String): Flow<List<Movie>>
}