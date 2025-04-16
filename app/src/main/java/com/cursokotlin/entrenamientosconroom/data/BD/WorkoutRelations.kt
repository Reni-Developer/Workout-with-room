package com.cursokotlin.entrenamientosconroom.data.BD

import androidx.room.Embedded
import androidx.room.Relation


data class WorkoutWithSetsAndExercises(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "workoutid",
        entityColumn = "workoutid",
        entity = WorkoutSet::class
    )
    val sets: List<SetWithExercise>
)

data class SetWithExercise(
    @Embedded val set: WorkoutSet,
    @Relation(
        parentColumn = "workoutsetid",
        entityColumn = "workoutsetid",
        entity = Exercise::class
    )
    val exercises: List<Exercise>
)