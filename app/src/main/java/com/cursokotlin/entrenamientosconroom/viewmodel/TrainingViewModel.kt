package com.cursokotlin.entrenamientosconroom.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutDao
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutWithSetsAndExercises
import com.cursokotlin.entrenamientosconroom.data.networkAPI.UserDataModel
import com.cursokotlin.entrenamientosconroom.dominio.TrainingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor(
    private val trainingUseCase: TrainingUseCase,
    private val workoutDao: WorkoutDao
) : ViewModel() {

    private val _workoutWithSets = MutableLiveData<List<WorkoutWithSetsAndExercises>>()
    val workoutWithSets: LiveData<List<WorkoutWithSetsAndExercises>> get() = _workoutWithSets

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _age = MutableLiveData<Int>(0)
    val age: LiveData<Int> get() = _age

    private val _time = MutableLiveData<Int>(0)
    val time: LiveData<Int> get() = _time

    private val _injure = MutableLiveData<String>("")
    val injure: LiveData<String> get() = _injure

    private val _sex = MutableLiveData<Int>(0)
    val sex: LiveData<Int> get() = _sex

    private val _language = MutableLiveData<Int>(0)
    val language: LiveData<Int> get() = _language

    private val _target = MutableLiveData<Int>(0)
    val target: LiveData<Int> get() = _target

    private val _difficulty = MutableLiveData<Int>(0)
    val difficulty: LiveData<Int> get() = _difficulty

    private val _muscles = MutableLiveData<List<Int>>(listOf())
    val muscles: LiveData<List<Int>> get() = _muscles

    fun onChangeAge(age: Int) {
        _age.value = age
    }

    fun onChangeTime(time: Int) {
        _time.value = time
    }

    fun onChangeInjure(injure: String) {
        _injure.value = injure
        Log.d("TrainingViewModel", "onChangeInjure: $injure")
    }

    fun onChangeSex(sex: Int) {
        _sex.value = sex
    }

    fun onChangeLanguage(language: Int) {
        _language.value = language
    }

    fun onChangeTarget(target: Int) {
        _target.value = target
    }

    fun onChangeDifficulty(difficulty: Int) {
        _difficulty.value = difficulty
    }

    fun onChangeMuscles(muscles: List<Int>) {
        _muscles.value = muscles
    }

    fun loadWorkout(userData: UserDataModel) {
        // Paso 1: Llama al UseCase que hace POST a la API y guarda en Room
        _isLoading.value = true
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
        _isLoading.value = false
    }

}


