package com.cursokotlin.entrenamientosconroom.data.networkAPI

import android.util.Log
import com.cursokotlin.entrenamientosconroom.data.bd.Exercise
import com.cursokotlin.entrenamientosconroom.data.bd.ExerciseDao
import com.cursokotlin.entrenamientosconroom.data.bd.SetDao
import com.cursokotlin.entrenamientosconroom.data.bd.Workout
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutDao
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutSet
import javax.inject.Inject


class TrainingService @Inject constructor(
    private val trainingClient: TrainingClient,
    private val workoutDao: WorkoutDao,
    private val setDao: SetDao,
    private val exerciseDao: ExerciseDao
) {
    suspend fun fetchAndSaveTraining(userData: UserDataModel) {
        try {
            val response = trainingClient.doWorkout(userData = userData)
            if (response.isSuccessful) {
                response.body()?.let { workoutModel ->
                    saveToLocalDatabase(workoutModel)
                }
            } else {
                Log.e("TrainingService", "Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("TrainingService", "Error en fetchAndSaveWorkout", e)
        }
    }

    suspend fun saveToLocalDatabase(workoutModel: WorkoutModel) {
// 🔁 Aquí se guarda el modelo WorkoutModel en la base de datos local.
        val workoutId = workoutDao.insert(Workout(name = workoutModel.name, coach_explanation = workoutModel.coach_explanation))

        workoutModel.sets.forEach { set ->
            val setId = setDao.insert(WorkoutSet(workoutId = workoutId, rounds = set.rounds, order_set_id = set.order_set_id))

            set.exercises.forEach { ex ->
                exerciseDao.insert(Exercise(
                        workoutSetId = setId,
                        order_exercise_id = ex.order_exercise_id,
                        name = ex.name,
                        language_name = ex.language_name,
                        reps = ex.reps,
                        movement_id = ex.movement_id,
                        muscle_id = ex.muscle_id,
                        url = ex.url
                    )
                )
            }
        }
    }
}
