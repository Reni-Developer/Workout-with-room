package com.cursokotlin.entrenamientosconroom.data.networkAPI

import com.google.gson.annotations.SerializedName

data class TrainingResponse(
    @SerializedName("sex") val sex: Int,
    @SerializedName("age") val age: Int,
    @SerializedName("target")val target: Int,
    @SerializedName("muscles")val muscles: List<Int>,
    @SerializedName("difficulty")val difficulty: Int,
    @SerializedName("time")val time: Int,
    @SerializedName("injuries")val injuries: String,
    @SerializedName("language")val language: Int
)
