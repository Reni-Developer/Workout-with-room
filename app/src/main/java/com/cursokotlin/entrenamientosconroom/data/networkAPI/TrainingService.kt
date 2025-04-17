package com.cursokotlin.entrenamientosconroom.data.networkAPI

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class TrainingService @Inject constructor(private val trainingClient: TrainingClient) {

    suspend fun doWorkout(): TrainingResponse {
        return withContext(Dispatchers.IO) {
            val response = trainingClient.doWorkout()
            response.body().age == 20
        }
    }