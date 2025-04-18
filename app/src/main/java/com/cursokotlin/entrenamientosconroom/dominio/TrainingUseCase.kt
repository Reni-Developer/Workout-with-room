package com.cursokotlin.entrenamientosconroom.dominio

import com.cursokotlin.entrenamientosconroom.data.networkAPI.TrainingRepository
import com.cursokotlin.entrenamientosconroom.data.networkAPI.UserDataModel
import javax.inject.Inject

class TrainingUseCase @Inject constructor(private val trainingRepository: TrainingRepository) {
    //El operator fun invoke(...), permite que  se pueda llamar a este use case como si fuera una función directa
    suspend operator fun invoke(userData: UserDataModel) {
        return trainingRepository.fetchAndSaveTraining(userData)
    }
}