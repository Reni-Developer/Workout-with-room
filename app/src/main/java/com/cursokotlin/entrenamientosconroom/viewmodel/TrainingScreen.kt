@file:Suppress("DEPRECATION")

package com.cursokotlin.entrenamientosconroom.viewmodel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrainingScreen(modifier: Modifier = Modifier, trainingViewModel: TrainingViewModel) {

    val workoutWithSetsAndExercises by trainingViewModel.workoutWithSets.observeAsState(emptyList())
    val week = arrayOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes")

    Column(modifier = modifier.fillMaxSize()) {
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            itemsIndexed(week) { index, day ->
                val id = index + 1
                Button(modifier = modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                    shape = ButtonDefaults.elevatedShape,
                    colors = ButtonDefaults.buttonColors(Color(0xF32C4A9F)),
                    onClick = { trainingViewModel.changeWorkoutId(id) })
                {
                    Text(text = day)
                }
            }
        }
        Card(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            colors = CardDefaults.cardColors(Color(0xF32C4A9F))
        ) {
            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                items(
                    workoutWithSetsAndExercises,
                    key = { it.workout.workoutid }) { workoutWithSets ->
                    Text(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        text = "Entrenamiento: ${workoutWithSets.workout.entrenamiento} "
                    )
                    Text(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.End,
                        text = "(${workoutWithSets.workout.duracion} min)"
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(
                        thickness = 1.dp,
                        color = Color(0xFF414141),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    workoutWithSets.sets.forEach { setWithExercise ->
                        Text(
                            color = Color(0xFF282828),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            text = "Set${setWithExercise.set.orden}:"
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            text = setWithExercise.set.workoutset
                        )
                        setWithExercise.exercises.forEach { exercise ->
                            Text(
                                color = Color.White,
                                text = "Exercise: ${exercise.exercise}, -> ${exercise.reps} Reps, (${exercise.weight.toInt()} lb)"
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}




