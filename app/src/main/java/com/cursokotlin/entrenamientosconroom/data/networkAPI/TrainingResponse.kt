package com.cursokotlin.entrenamientosconroom.data.networkAPI

data class ExerciseModel(
    val order_exercise_id: Int,
    val name: String,
    val language_name: String,
    val reps: String,
    val movement_id: Int,
    val muscle_id: Int,
    val url: String
)

data class SetModel(
    val exercises: List<ExerciseModel>,
    val rounds: Int,
    val order_set_id: Int
)

data class WorkoutModel(
    val name: String,
    val coach_explanation: String,
    val sets: List<SetModel>
)
