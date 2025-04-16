package com.cursokotlin.entrenamientosconroom.data.networkAPI

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
