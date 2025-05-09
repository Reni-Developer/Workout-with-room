package com.cursokotlin.entrenamientosconroom.ui.screenHome

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
class HomeViewModel @Inject constructor(
    private val trainingUseCase: TrainingUseCase,
    private val workoutDao: WorkoutDao
) : ViewModel() {

    private val _workoutWithSets = MutableLiveData<List<WorkoutWithSetsAndExercises>>()
    val workoutWithSets: LiveData<List<WorkoutWithSetsAndExercises>> get() = _workoutWithSets

    private val _lastUserData = MutableLiveData<UserDataModel>(
        UserDataModel(1, 30, 0, listOf(), 1, 60, "", 1)
    )
    val lastUserData: LiveData<UserDataModel> get() = _lastUserData

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadWorkout(currentUserData: UserDataModel) {
        _isLoading.value = true
        // Paso 1: Llama al UseCase que hace POST a la API y guarda en Room
        viewModelScope.launch {
            trainingUseCase(currentUserData)
            // Paso 2: Recupera todos los workouts simples (sin relaciones)
            val allWorkouts = workoutDao.getAllWorkouts()
            // Paso 3: Recorre cada workout y obtiene su estructura completa (sets + ejercicios)
            val detailedWorkouts = mutableListOf<WorkoutWithSetsAndExercises>()
            for (workout in allWorkouts) {
                workoutDao.getWorkoutWithSetsAndExercises(workout.workoutId.toInt())?.let {
                    detailedWorkouts += it
                }
            }
            _workoutWithSets.value = detailedWorkouts
            _isLoading.value = false
        }
        _lastUserData.value = currentUserData
    }
}