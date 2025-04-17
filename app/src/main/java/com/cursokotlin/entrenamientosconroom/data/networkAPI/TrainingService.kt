package com.cursokotlin.entrenamientosconroom.data.networkAPI

import android.util.Log
import com.cursokotlin.entrenamientosconroom.data.BD.Exercise
import com.cursokotlin.entrenamientosconroom.data.BD.ExerciseDao
import com.cursokotlin.entrenamientosconroom.data.BD.SetDao
import com.cursokotlin.entrenamientosconroom.data.BD.Workout
import com.cursokotlin.entrenamientosconroom.data.BD.WorkoutDao
import com.cursokotlin.entrenamientosconroom.data.BD.WorkoutSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.log


class TrainingService @Inject constructor(
    private val trainingClient: TrainingClient,
    private val workoutDao: WorkoutDao,
    private val setDao: SetDao,
    private val exerciseDao: ExerciseDao
) {
    suspend fun fetchAndSaveTraining() {
        try {
            val response = trainingClient.doWorkout()
            if (response.isSuccessful) {
                response.body()?.let { trainingResponse ->
                    saveToLocalDatabase(trainingResponse)
                }
            } else {
                Log.e("TrainingService", "Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("TrainingService", "Error en fetchAndSaveWorkout", e)
        }
    }

    private fun saveToLocalDatabase(trainingResponse: TrainingResponse) {
// 🔁 Aquí se adapta el TrainingResponse a las entidades locales.
        val workout = Workout(entrenamiento = "Entrenamiento generado", duracion = trainingResponse.time)
        val workoutId = workoutDao.insert(workout)

        // ⚙️ En este ejemplo simple, creamos un solo set con ejercicios ficticios por músculo.
        val set = WorkoutSet(workoutid = workoutId, workoutset = "Serie generada", orden = 1)
        val setId = setDao.insert(set)

        trainingResponse.muscles.forEachIndexed { index, muscleId ->
            val exercise = Exercise(workoutsetid = setId, exercise = "Ejercicio para músculo $muscleId", reps = 10 + index, weight = 20f + index * 2)
            exerciseDao.insert(exercise)
        }
    }
}