package com.cursokotlin.entrenamientosconroom.ui.screenUser

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingViewModel @Inject constructor() : ViewModel() {

    private val _sheetState = MutableLiveData<Boolean>(false)
    val sheetState: LiveData<Boolean> get() = _sheetState

    private val _age = MutableLiveData<Int>(30)
    val age: LiveData<Int> get() = _age
    private val _currentAge = MutableLiveData<String>("")
    val currentAge: LiveData<String> get() = _currentAge

    private val _time = MutableLiveData<Int>(30)
    val time: LiveData<Int> get() = _time
    private val _currentTime = MutableLiveData<String>("")
    val currentTime: LiveData<String> get() = _currentTime

    private val _injure = MutableLiveData<String>("ninguna")
    val injure: LiveData<String> get() = _injure
    private val _currentInjuries = MutableLiveData<String>("")
    val currentInjuries: LiveData<String> get() = _currentInjuries

    private val _sex = MutableLiveData<Int>(1)
    val sex: LiveData<Int> get() = _sex

    private val _language = MutableLiveData<Int>(0)
    val language: LiveData<Int> get() = _language

    private val _target = MutableLiveData<Int>(0)
    val target: LiveData<Int> get() = _target

    private val _difficulty = MutableLiveData<Int>(1)
    val difficulty: LiveData<Int> get() = _difficulty

    private val _muscles = MutableLiveData<List<Int>>(listOf(50,11,80))
    val muscles: LiveData<List<Int>> get() = _muscles

    private val _singOutDialogState = MutableLiveData<Boolean>(false)
    val singOutDialogState: LiveData<Boolean> get() = _singOutDialogState

    private val _changeColorMaleSelected = MutableLiveData<Boolean>(true)
    val changeColorMSelected: LiveData<Boolean> get() = _changeColorMaleSelected

    private val _changeColorFemaleSelected = MutableLiveData<Boolean>(false)
    val changeColorFSelected: LiveData<Boolean> get() = _changeColorFemaleSelected

    val _musclesById = MutableLiveData<List<CheckInfo>>(
        listOf(
            CheckInfo("Pecho", false, 50),
            CheckInfo("Tríceps", false, 31),
            CheckInfo("Hombros", false, 90),
            CheckInfo("Espalda", false, 10),
            CheckInfo("Bíceps", false, 30),
            CheckInfo("Antebrazos", false, 60),
            CheckInfo("Trapecios", false, 20),
            CheckInfo("Cuádriceps", false, 11),
            CheckInfo("Glúteos", false, 70),
            CheckInfo("Isquiotibiales", false, 40),
            CheckInfo("Pantorrillas", false, 21),
            CheckInfo("Aductores", false, 51),
            CheckInfo("Abductores", false, 61),
            CheckInfo("Abdomen", false, 80),
            CheckInfo("Espalda baja", false, 41)
        )
    )
    val musclesById: LiveData<List<CheckInfo>> get() = _musclesById

    fun updateMuscleSelection(index: Int, selected: Boolean) {
        val updatedList = _musclesById.value?.toMutableList()
        // este bloque se ejecuta solo si updatedList NO es null
        updatedList?.let {
            it[index] = it[index].copy(selected = selected)
            _musclesById.value = it
            val selectedMuscles = it.filter { it.selected }.mapNotNull { it.id }
            _muscles.value = selectedMuscles
        }
    }

    fun onChangeAge(age: Int?) {
        _age.value = age!!
    }
    fun onChangeCurrentAge(currentAge: String) {
        _currentAge.value = currentAge
        Log.d("TrainingViewModel", "onChangeCurrentAge: $currentAge")
    }

    fun onChangeTime(time: Int) {
        _time.value = time
        Log.d("TrainingViewModel","onChangeTime:$time")
    }
    fun onChangeCurrentTime(currentTime: String){
        _currentTime.value = currentTime
        Log.d("TrainingViewModel","onChangeCurrentTime: $currentTime")
    }

    fun onChangeInjure(injure: String) {
        _injure.value = injure
        Log.d("TrainingViewModel", "onChangeInjure: $injure")
    }
    fun onChangeCurrentInjure(currentInjure: String){
        _currentInjuries.value = currentInjure
        Log.d("TrainingViewModel", "onChangeCurrentInjure: $currentInjure")
    }

    fun onChangeSex(sex: Int) {
        _sex.value = sex
        Log.d("TrainingViewModel","Variable sex 0 -> F / 1 -> M: El sexo es -> ${_sex.value}")
        _changeColorMaleSelected.value = _changeColorMaleSelected.value != true
        _changeColorFemaleSelected.value = _changeColorFemaleSelected.value != true
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

    fun openSingOutDialog(){
        _singOutDialogState.value = true
    }

    fun closeSingOutDialog(){
        _singOutDialogState.value = false
    }

    fun onChangeSheetState() {
        _sheetState.value = _sheetState.value != true
        Log.d("TrainingViewModel", "onChangeSheetState: ${_sheetState.value}")
    }
}