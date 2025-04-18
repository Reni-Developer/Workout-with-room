package com.cursokotlin.entrenamientosconroom.data.bd

import androidx.room.Embedded
import androidx.room.Relation


data class WorkoutWithSetsAndExercises(
    @Embedded var workout: Workout,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId",
        entity = WorkoutSet::class
    )
    val sets: List<SetWithExercise>
)

data class SetWithExercise(
    @Embedded var set: WorkoutSet,
    @Relation(
        parentColumn = "workoutSetId",
        entityColumn = "workoutSetId",
        entity = Exercise::class
    )
    val exercises: List<Exercise>
)