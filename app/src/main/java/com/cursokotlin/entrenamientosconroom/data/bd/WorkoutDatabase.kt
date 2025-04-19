package com.cursokotlin.entrenamientosconroom.data.bd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(
    entities = [Workout::class, WorkoutSet::class, Exercise::class],
    exportSchema = false,
    version = 1
)

abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun setDao(): SetDao
    abstract fun exerciseDao(): ExerciseDao


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