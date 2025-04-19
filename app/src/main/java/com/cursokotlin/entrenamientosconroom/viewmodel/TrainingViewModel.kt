package com.cursokotlin.entrenamientosconroom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutDao
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutWithSetsAndExercises
import com.cursokotlin.entrenamientosconroom.data.networkAPI.UserDataModel
import com.cursokotlin.entrenamientosconroom.dominio.TrainingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val trainingUseCase: TrainingUseCase,
    private val workoutDao: WorkoutDao
) : ViewModel() {

    private val _workoutWithSets = MutableLiveData<List<WorkoutWithSetsAndExercises>>()
    val workoutWithSets: LiveData<List<WorkoutWithSetsAndExercises>> get() = _workoutWithSets

    fun loadWorkout(userData: UserDataModel) {
        // Paso 1: Llama al UseCase que hace POST a la API y guarda en Room
        viewModelScope.launch {
            trainingUseCase(userData)

            // Paso 2: Recupera todos los workouts simples (sin relaciones)
            val allWorkouts = workoutDao.getAllWorkouts()

            // Paso 3: Recorre cada workout y obtiene su estructura completa (sets + ejercicios)
            val detailedWorkouts = mutableListOf<WorkoutWithSetsAndExercises>()
            for (workout in allWorkouts) {
               detailedWorkouts += workoutDao.getWorkoutWithSetsAndExercises(workout.workoutId.toInt())
            }
            _workoutWithSets.value = detailedWorkouts
        }
    }
}


