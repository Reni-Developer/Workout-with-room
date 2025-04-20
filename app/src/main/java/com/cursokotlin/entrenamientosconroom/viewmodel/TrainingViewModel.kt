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

    private val _age = MutableLiveData<Int>(25)
    val age: LiveData<Int> get() = _age

    private val _time = MutableLiveData<Int>(30)
    val time: LiveData<Int> get() = _time

    private val _injure = MutableLiveData<String>("ninguna")
    val injure: LiveData<String> get() = _injure

    private val _sex = MutableLiveData<Int>(1)
    val sex: LiveData<Int> get() = _sex

    private val _language = MutableLiveData<Int>(1)
    val language: LiveData<Int> get() = _language

    private val _target = MutableLiveData<Int>(0)
    val target: LiveData<Int> get() = _target

    private val _difficulty = MutableLiveData<Int>(2)
    val difficulty: LiveData<Int> get() = _difficulty

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

    fun onChangeAge(age: Int) {
        _age.value = age
    }

    fun onChangeTime(time: Int) {
        _time.value = time
    }

    fun onChangeInjure(injure: String) {
        _injure.value = injure
    }

    fun onChangeSex(sex: Int) {
        _sex.value = sex
    }

    fun onChangeLanguage(language: Int) {
        _language.value = language
    }

}


