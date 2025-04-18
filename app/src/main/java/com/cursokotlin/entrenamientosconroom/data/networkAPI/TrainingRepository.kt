package com.cursokotlin.entrenamientosconroom.data.networkAPI

import javax.inject.Inject

//Clean architecture: el repositorio actúa como intermediario entre la capa de dominio y la de red/base de datos.
class TrainingRepository @Inject constructor(private val trainingService: TrainingService)  {

    suspend fun fetchAndSaveTraining(userData: UserDataModel){
        return trainingService.fetchAndSaveTraining(userData)
    }

}