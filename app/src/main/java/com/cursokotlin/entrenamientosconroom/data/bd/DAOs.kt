package com.cursokotlin.entrenamientosconroom.data.bd

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface WorkoutDao {
    @Insert suspend fun insert(workout: Workout): Long
    @Update suspend fun update(workout: Workout)
    @Delete suspend fun delete(workout: Workout)
    @Delete suspend fun deleteAll(workouts: List<Workout>)

    @Transaction
    @Query("SELECT * FROM Workout WHERE workoutid = :id ")
    suspend fun getWorkoutWithSetsAndExercises(id:Int): List<WorkoutWithSetsAndExercises>

    @Query("SELECT * FROM Workout")
    suspend fun getAllWorkouts(): List<Workout>
}

@Dao
interface SetDao {
    @Insert suspend fun insert(set: WorkoutSet): Long
    @Delete suspend fun delete(set: WorkoutSet)
}

@Dao
interface ExerciseDao {
    @Insert suspend fun insert(exercise: Exercise): Long
    @Delete suspend fun delete(exercise: Exercise)
}