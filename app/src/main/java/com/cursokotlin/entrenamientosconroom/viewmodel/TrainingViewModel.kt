package com.cursokotlin.entrenamientosconroom.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cursokotlin.entrenamientosconroom.data.bd.Exercise
import com.cursokotlin.entrenamientosconroom.data.bd.Workout
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutDatabase.Companion.getDatabase
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutSet
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutWithSetsAndExercises
import kotlinx.coroutines.launch

//Una de las formas más comunes para pasarle dependencias
// al ViewModel (como DAOs o repositorios) es a través de una
// clase que extienda de Application. Si no se implementa Dagger Hilt.
class TrainingViewModel(application: Application) : AndroidViewModel(application) {


    private val workoutDao = getDatabase(application).workoutDao()
    private val setDao = getDatabase(application).setDao()
    private val exerciseDao = getDatabase(application).exerciseDao()

    private val _workoutWithSets = MutableLiveData<List<WorkoutWithSetsAndExercises>>()
    val workoutWithSets: LiveData<List<WorkoutWithSetsAndExercises>> get() = _workoutWithSets

    private var _changeWorkoutId = MutableLiveData<Int>(0)
    val changeWorkoutId: Int = _changeWorkoutId.value ?: 0

    init {
        viewModelScope.launch {
            insertBD()
        }
    }

    fun changeWorkoutId (numberBdd: Int){
        viewModelScope.launch{
            _changeWorkoutId.value =  numberBdd
            loadWorkoutById(numberBdd)
        }
    }

    fun loadWorkoutById(changeWorkoutId: Int) {
        viewModelScope.launch {
            _workoutWithSets.value =
                workoutDao.getWorkoutWithSetsAndExercises(changeWorkoutId)
        }
    }

    private suspend fun insertBD() {

    }
}


