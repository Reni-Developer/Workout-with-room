package com.cursokotlin.entrenamientosconroom.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cursokotlin.entrenamientosconroom.data.BD.Exercise
import com.cursokotlin.entrenamientosconroom.data.BD.Workout
import com.cursokotlin.entrenamientosconroom.data.BD.WorkoutDatabase.Companion.getDatabase
import com.cursokotlin.entrenamientosconroom.data.BD.WorkoutSet
import com.cursokotlin.entrenamientosconroom.data.BD.WorkoutWithSetsAndExercises
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

        if (workoutDao.getWorkoutWithSetsAndExercises(changeWorkoutId).isNotEmpty()) return

        //1

        val workout1Id = workoutDao.insert(Workout(entrenamiento = "Piernas", duracion = 60 ))

        val set1Id = setDao.insert(WorkoutSet(workoutset = "Serie Simple", orden = 1, workoutid = workout1Id))
        val set2Id = setDao.insert(WorkoutSet(workoutset = "Super Serie", orden = 2, workoutid = workout1Id))
        val set3Id = setDao.insert(WorkoutSet(workoutset = "Circuito", orden = 3, workoutid = workout1Id))

        exerciseDao.insert(Exercise(exercise = "Sentadilla", reps = 10, weight = 100f, workoutsetid = set1Id))
        exerciseDao.insert(Exercise(exercise = "Prensa", reps = 10, weight = 200f, workoutsetid = set2Id))
        exerciseDao.insert(Exercise(exercise = "Cuclillas", reps = 10, weight = 0f, workoutsetid = set2Id))
        exerciseDao.insert(Exercise(exercise = "Pantorrilas", reps = 10, weight = 100f, workoutsetid = set3Id))
        exerciseDao.insert(Exercise(exercise = "Saltos en el lugar", reps = 10, weight = 0f, workoutsetid = set3Id))
        exerciseDao.insert(Exercise(exercise = "Spring", reps = 10, weight = 0f, workoutsetid = set3Id))

        //2

        val workoutId2 = workoutDao.insert(Workout(entrenamiento = "Pecho y triceps", duracion = 60))

        val set4Id = setDao.insert(WorkoutSet(workoutset = "Circuito", orden = 1, workoutid = workoutId2))
        val set5Id = setDao.insert(WorkoutSet(workoutset = "Serie Simple", orden = 2, workoutid = workoutId2))
        val set6Id = setDao.insert(WorkoutSet(workoutset = "Super Serie", orden = 3, workoutid = workoutId2))

        exerciseDao.insert(Exercise(exercise = "Triceps polea", reps = 10, weight = 100f, workoutsetid = set4Id))
        exerciseDao.insert(Exercise(exercise = "Toritos", reps = 10, weight = 0f, workoutsetid = set4Id))
        exerciseDao.insert(Exercise(exercise = "Paralelas", reps = 10, weight = 0f, workoutsetid = set4Id))
        exerciseDao.insert(Exercise(exercise = "Press de banca", reps = 10, weight = 100f, workoutsetid = set5Id))
        exerciseDao.insert(Exercise(exercise = "Aperturas", reps = 10, weight = 200f, workoutsetid = set6Id))
        exerciseDao.insert(Exercise(exercise = "Press de mascuernas", reps = 10, weight = 0f, workoutsetid = set6Id))

        //3

        val workoutId3 = workoutDao.insert(Workout(entrenamiento = "Espalda y bíceps", duracion = 50))

        val set7Id = setDao.insert(WorkoutSet(workoutset = "SuperSerie", orden = 1, workoutid = workoutId3))
        val set8Id = setDao.insert(WorkoutSet(workoutset = "SuperSerie", orden = 2, workoutid = workoutId3))
        val set9Id = setDao.insert(WorkoutSet(workoutset = "Circuito", orden = 3, workoutid = workoutId3))

        exerciseDao.insert(Exercise(exercise = "Remo con barra", reps = 10, weight = 80f, workoutsetid = set7Id))
        exerciseDao.insert(Exercise(exercise = "Peso muerto", reps = 10, weight = 120f, workoutsetid = set7Id))
        exerciseDao.insert(Exercise(exercise = "Curl con barra", reps = 12, weight = 40f, workoutsetid = set8Id))
        exerciseDao.insert(Exercise(exercise = "Curl martillo", reps = 12, weight = 30f, workoutsetid = set8Id))
        exerciseDao.insert(Exercise(exercise = "Dominadas", reps = 8, weight = 0f, workoutsetid = set9Id))
        exerciseDao.insert(Exercise(exercise = "Biceps en polea", reps = 8, weight = 60f, workoutsetid = set9Id))
        exerciseDao.insert(Exercise(exercise = "Face pull", reps = 15, weight = 20f, workoutsetid = set9Id))

        //4

        val workoutId4 = workoutDao.insert(Workout(entrenamiento = "Hombros y abdomen", duracion = 45))

        val set10Id = setDao.insert(WorkoutSet(workoutset = "Serie Simple", orden = 1, workoutid = workoutId4))
        val set11Id = setDao.insert(WorkoutSet(workoutset = "Super Serie", orden = 2, workoutid = workoutId4))
        val set12Id = setDao.insert(WorkoutSet(workoutset = "Circuito", orden = 3, workoutid = workoutId4))

        exerciseDao.insert(Exercise(exercise = "Elevaciones laterales", reps = 12, weight = 15f, workoutsetid = set10Id))
        exerciseDao.insert(Exercise(exercise = "Press militar", reps = 10, weight = 40f, workoutsetid = set11Id))
        exerciseDao.insert(Exercise(exercise = "Pájaros", reps = 12, weight = 10f, workoutsetid = set11Id))
        exerciseDao.insert(Exercise(exercise = "Crunch en máquina", reps = 15, weight = 25f, workoutsetid = set12Id))
        exerciseDao.insert(Exercise(exercise = "Elevación de piernas", reps = 15, weight = 0f, workoutsetid = set12Id))
        exerciseDao.insert(Exercise(exercise = "Plancha", reps = 1, weight = 0f, workoutsetid = set12Id))

        //5

        val workoutId5 = workoutDao.insert(Workout(entrenamiento = "Glúteos y femorales", duracion = 55))

        val set13Id = setDao.insert(WorkoutSet(workoutset = "Super Serie", orden = 1, workoutid = workoutId5))
        val set14Id = setDao.insert(WorkoutSet(workoutset = "Serie Simple", orden = 2, workoutid = workoutId5))
        val set15Id = setDao.insert(WorkoutSet(workoutset = "Circuito", orden = 3, workoutid = workoutId5))

        exerciseDao.insert(Exercise(exercise = "Hip thrust", reps = 12, weight = 100f, workoutsetid = set13Id))
        exerciseDao.insert(Exercise(exercise = "Patada de glúteo", reps = 15, weight = 25f, workoutsetid = set13Id))
        exerciseDao.insert(Exercise(exercise = "Curl femoral", reps = 12, weight = 35f, workoutsetid = set14Id))
        exerciseDao.insert(Exercise(exercise = "Zancadas", reps = 12, weight = 20f, workoutsetid = set15Id))
        exerciseDao.insert(Exercise(exercise = "Elevación de pelvis", reps = 15, weight = 0f, workoutsetid = set15Id))
        exerciseDao.insert(Exercise(exercise = "Puente con banda", reps = 20, weight = 0f, workoutsetid = set15Id))
    }
}


