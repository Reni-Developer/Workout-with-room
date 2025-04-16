package com.cursokotlin.entrenamientosconroom.data.BD

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "Workout")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val workoutid: Long = 0,
    @ColumnInfo(name = "entrenamiento")
    val entrenamiento: String,
    @ColumnInfo(name = "duracion")
    val duracion: Int
)

@Entity(
    tableName = "WorkoutSet",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["workoutid"],
            childColumns = ["workoutid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["workoutid"])]
)
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true)
    val workoutsetid: Long = 0,
    val workoutid: Long = 0,
    @ColumnInfo(name = "workoutset")
    val workoutset: String,
    @ColumnInfo(name = "orden")
    val orden: Int
)

@Entity(
    tableName = "Exercise",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSet::class,
            parentColumns = ["workoutsetid"],
            childColumns = ["workoutsetid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["workoutsetid"])]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val exerciseid: Long = 0,
    val workoutsetid: Long = 0,
    @ColumnInfo(name = "exercise")
    val exercise: String,
    @ColumnInfo(name = "reps")
    val reps: Int,
    @ColumnInfo(name = "weight")
    val weight: Float,
)
