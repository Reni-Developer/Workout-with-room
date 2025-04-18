package com.cursokotlin.entrenamientosconroom.data.networkAPI

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TrainingClient {
    @POST("/coach/generate_workout")
    suspend fun doWorkout(
        @Header("X-API-Key") apiKey: String = "Wt5=+y%.XI!,gK.I?xgQ",
        @Body userData: UserDataModel
    ): Response<WorkoutModel>
}

data class UserDataModel(
    val sex: Int,
    val age: Int,
    val target: Int,
    val muscles: List<Int>,
    val difficulty: Int,
    val time: Int,
    val injuries: String,
    val language: Int
)