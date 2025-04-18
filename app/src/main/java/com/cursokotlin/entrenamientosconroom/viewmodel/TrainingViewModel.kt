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
        // 1. Llama al API y guarda en BD
        viewModelScope.launch {
            trainingUseCase(userData)

            // 2. Consulta los datos ya guardados
            val allWorkouts = workoutDao.getAllWorkouts()
            val detailedWorkouts = allWorkouts.flatMap {
                workoutDao.getWorkoutWithSetsAndExercises(it.workoutId.toInt())
            }
            _workoutWithSets.value = detailedWorkouts
        }
    }
}


