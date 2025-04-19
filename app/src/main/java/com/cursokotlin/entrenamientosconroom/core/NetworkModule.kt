package com.cursokotlin.entrenamientosconroom.core

import android.content.Context
import androidx.room.Room
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutDatabase
import com.cursokotlin.entrenamientosconroom.data.networkAPI.TrainingClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //Para que viva mientras la app viva
class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://coachai-server.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())//Convierte el json en clases de datos de Kotlin
            .build()
    }
    @Singleton
    @Provides
    fun provideTrainingClient(retrofit: Retrofit): TrainingClient {
        return retrofit.create(TrainingClient::class.java)
    }

}