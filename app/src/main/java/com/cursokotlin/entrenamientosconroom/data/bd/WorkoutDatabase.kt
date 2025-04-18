package com.cursokotlin.entrenamientosconroom.data.bd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Workout::class, WorkoutSet::class, Exercise::class],
    exportSchema = false,
    version = 1
)

abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun setDao(): SetDao
    abstract fun exerciseDao(): ExerciseDao


    companion object {
        @Volatile//para indicar que: Cualquier lectura o escritura de esa variable se
        // hace directamente en la memoria principal y no se guarda en caché por los hilos.
        // Esto garantiza que todos los hilos vean el valor más actualizado de la variable.
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(context: Context): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_database"
                ).fallbackToDestructiveMigration()
                    .build().also {
                        INSTANCE = it
                    }
            }
        }
    }
}