@file:Suppress("DEPRECATION")

package com.cursokotlin.entrenamientosconroom.ui

import androidx.collection.MutableIntList
import androidx.collection.mutableIntListOf
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.cursokotlin.entrenamientosconroom.data.networkAPI.UserDataModel
import com.cursokotlin.entrenamientosconroom.viewmodel.TrainingViewModel

@Composable
fun TrainingScreen(modifier: Modifier = Modifier, trainingViewModel: TrainingViewModel) {

    val workoutWithSetsAndExercises by trainingViewModel.workoutWithSets.observeAsState(emptyList())

    val sex = 1
    val age = 25
    val target = 0
    val muscles = listOf(50, 31)
    val difficulty = 2
    val time = 60
    val injure = "ninguna"
    val language = 1

    val currentUserData = remember {
        UserDataModel(
            sex = sex,
            age = age,
            target = target,
            muscles = muscles,
            difficulty = difficulty,
            time = time,
            injuries = injure,
            language = language
        )
    }
    var lastUserData by remember { mutableStateOf<UserDataModel?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        Button(modifier = modifier
            .padding(horizontal = 8.dp),
            shape = ButtonDefaults.elevatedShape,
            colors = ButtonDefaults.buttonColors(Color(0xF32C4A9F)),
            onClick = {
                if (lastUserData != currentUserData) {
                    trainingViewModel.loadWorkout(currentUserData)
                    lastUserData = currentUserData
                }
            }
        )
        {
            Text(text = "Star Training")
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
                    key = { it.workout.workoutId }) { workoutWithSets ->
                    Text(
                        color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp,
                        text = "Entrenamiento: ${workoutWithSets.workout.name} "
                    )
                    Text(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        textAlign = TextAlign.End,
                        text = "${workoutWithSets.workout.coach_explanation} "
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
                        val set = setWithExercise.set.order_set_id
                        val realSet = set + 1
                        Text(
                            color = Color(0xFF282828),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            text = "Set No.${realSet}"
                        )
                        Spacer(modifier = Modifier.size(8.dp))

                        Text(
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            text = "${setWithExercise.set.rounds} Rounds"
                        )
                        setWithExercise.exercises.forEach { exercise ->
                            Text(color = Color.White, text = "${exercise.order_exercise_id} ")
                            Text(color = Color.White, text = "Exercise: ${exercise.name}")
                            Text(color = Color.White, text = "Reps ${exercise.reps} ")
                            Text(color = Color.White, text = "movement ${exercise.movement_id}")
                            Text(color = Color.White, text = "muscle ${exercise.muscle_id}")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}





