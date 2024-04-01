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
    fun insertAll(vararg workouts: Movie)

    @Query("SELECT * FROM movie")
    fun getAll(): Flow<List<Movie>>
}