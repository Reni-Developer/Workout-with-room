package com.cursokotlin.entrenamientosconroom.data.networkAPI

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface TrainingClient {
    @GET("/coach/generate_workout")
    suspend fun doWorkout(@Header("X-API-Key") apiKey: String = "Wt5=+y%.XI!,gK.I?xgQ" ):Response<TrainingResponse>
}