@file:Suppress("DEPRECATION")

package com.cursokotlin.entrenamientosconroom.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cursokotlin.entrenamientosconroom.data.networkAPI.UserDataModel
import com.cursokotlin.entrenamientosconroom.viewmodel.TrainingViewModel

@Composable
fun TrainingScreen(modifier: Modifier = Modifier, trainingViewModel: TrainingViewModel) {

    val workoutWithSetsAndExercises by trainingViewModel.workoutWithSets.observeAsState(emptyList())
    val age by trainingViewModel.age.observeAsState(initial = 25)
    val time by trainingViewModel.time.observeAsState(initial = 25)
    val injuries by trainingViewModel.injure.observeAsState("ninguna")

    var muscles by remember { mutableStateOf(listOf(50, 31)) }
    var sex by remember { mutableIntStateOf(0) }
    var target by remember { mutableIntStateOf(0) }
    var difficulty by remember { mutableIntStateOf(2) }
    var language by remember { mutableIntStateOf(1) }

    val currentUserData = remember {
        UserDataModel(
            sex = sex,
            age = age,
            target = target,
            muscles = muscles,
            difficulty = difficulty,
            time = time,
            injuries = injuries,
            language = language
        )
    }

    Column(modifier = modifier.fillMaxSize()) {

        ConfigUserWorkout(Modifier, trainingViewModel, age, time, injuries)

        Button(modifier = Modifier
            .padding(horizontal = 8.dp),
            shape = ButtonDefaults.elevatedShape,
            colors = ButtonDefaults.buttonColors(Color(0xF32C4A9F)),
            onClick = { trainingViewModel.loadWorkout(currentUserData) }
        )
        {
            Text(text = "Update Training")
        }

        Card(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
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

@Composable
fun ConfigUserWorkout(
    modifier: Modifier = Modifier,
    trainingViewModel: TrainingViewModel,
    currentAge: Int, currentTime: Int, currentInjure: String) {

    var ageInt by remember { mutableIntStateOf(0) }
    var ageString by remember { mutableStateOf("") }
    var timeInt by remember { mutableIntStateOf(0) }
    var timeString by remember { mutableStateOf("") }
    var injure by remember { mutableStateOf("ninguna") }

    Card(modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
        TextField(
            value = ageString,
            onValueChange = {
                ageString = it
                if (ageString != "" && ageString.toIntOrNull() != null)
                    ageInt = ageString.toInt()
                },
            label = { Text(text = "Your Age") },
            singleLine = true,
            maxLines = 1,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        if (ageInt > 9 && ageInt < 100)
                        trainingViewModel.onChangeAge(ageInt)
                    },
                    tint = (
                            if (ageInt == currentAge)
                                Color.Green
                            else
                                Color.Black
                            )
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = timeString,
            onValueChange = {
                timeString = it
                if (timeString != "" && timeString.toIntOrNull() != null)
                    timeInt = timeString.toInt()
                },
            label = { Text(text = "Workout Time") },
            singleLine = true,
            maxLines = 1,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        if (timeInt > 9 && timeInt < 120)
                        trainingViewModel.onChangeTime(timeInt)
                    },
                    tint = (
                            if (timeInt == currentTime)
                                Color.Green
                            else
                                Color.Black
                            )
                )
            }
        )

        TextField(
            value = injure,
            onValueChange = { injure = it },
            label = { Text(text = "Injure") },
            singleLine = true,
            maxLines = 1,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        if (injure != "")
                        trainingViewModel.onChangeInjure(injure)
                    },
                    tint = (
                            if (injure == currentInjure)
                                Color.Green
                            else
                                Color.Black
                            )
                )
            }
        )

    }

}





