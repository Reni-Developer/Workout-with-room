package com.cursokotlin.entrenamientosconroom.data.bd

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "Workout")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val workoutId: Long = 0,
    val name: String,
    val coach_explanation: String,
)

@Entity(
    tableName = "WorkoutSet",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["workoutId"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["workoutid"])]
)
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true)
    val workoutSetId: Long = 0,
    val workoutId: Long = 0,
    val rounds: Int,
    val order_set_id: Int
)

@Entity(
    tableName = "Exercise",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSet::class,
            parentColumns = ["workoutSetId"],
            childColumns = ["workoutSetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["workoutsetid"])]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Long = 0,
    val workoutSetId: Long = 0,
    val order_exercise_id: Int,
    val name: String,
    val language_name: String,
    val reps: String,
    val movement_id: Int,
    val muscle_id: Int,
    val url: String
)
