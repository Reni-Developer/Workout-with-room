package com.cursokotlin.entrenamientosconroom.core

import android.content.Context
import androidx.room.Room
import com.cursokotlin.entrenamientosconroom.data.BD.WorkoutDatabase
import com.cursokotlin.entrenamientosconroom.data.networkAPI.TrainingClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //Para que viva mientras la app viva

class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://coachai-server.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())//Convierte el json en clases de datos de Kotlin
            .build()
    }

    @Singleton
    @Provides
    fun provideTrainingClient(retrofit: Retrofit): TrainingClient {
        return retrofit.create(TrainingClient::class.java)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object DatabaseModule {

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): WorkoutDatabase {
            return Room.databaseBuilder(context, WorkoutDatabase::class.java, "app_db")
                .fallbackToDestructiveMigration()
                .build()
        }

        @Provides
        fun provideWorkoutDao(db: WorkoutDatabase) = db.workoutDao()
        @Provides
        fun provideSetDao(db: WorkoutDatabase) = db.setDao()
        @Provides
        fun provideExerciseDao(db: WorkoutDatabase) = db.exerciseDao()
    }

}