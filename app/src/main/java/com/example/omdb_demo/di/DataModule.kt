package com.example.omdb_demo.di

import android.content.Context
import androidx.room.Room
import com.example.omdb_demo.data.MovieRepository
import com.example.omdb_demo.data.local.AppDatabase
import com.example.omdb_demo.data.local.MovieDao
import com.example.omdb_demo.data.remote.MovieService
import com.example.omdb_demo.data.remote.RemoteDataSource
import com.example.omdb_demo.domain.GetMovieByTitle
import com.example.omdb_demo.domain.GetMovies
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

/**
 *  hilt module responsible for injecting all local data related dependencies
 */
@InstallIn(SingletonComponent::class)
@Module
object DataModule {
    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext applicationContext: Context): AppDatabase =
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "database"
        ).build()

    @Singleton
    @Provides
    fun provideMovieDao(database: AppDatabase): MovieDao = database.getMovieDao()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Singleton
    @Provides
    fun provideRemoteDataSource(service: MovieService): RemoteDataSource =
        RemoteDataSource(service)

    @Singleton
    @Provides
    fun provideMovieRepositoryImpl(
        remoteDataSource: RemoteDataSource,
        movieDao: MovieDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): MovieRepository = MovieRepository(remoteDataSource, movieDao, ioDispatcher)

    @Singleton
    @Provides
    fun provideGetMovieByTitle(
        repository: MovieRepository
    ): GetMovieByTitle = GetMovieByTitle(repository)

    @Singleton
    @Provides
    fun provideGetMovies(
        repository: MovieRepository
    ): GetMovies = GetMovies(repository)
}