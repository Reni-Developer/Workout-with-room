@file:Suppress("DEPRECATION")

package com.cursokotlin.entrenamientosconroom.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cursokotlin.entrenamientosconroom.data.networkAPI.UserDataModel
import com.cursokotlin.entrenamientosconroom.viewmodel.TrainingViewModel
import kotlin.math.log

@Composable
fun TrainingScreen(modifier: Modifier = Modifier, trainingViewModel: TrainingViewModel) {

    val workoutWithSetsAndExercises
            by trainingViewModel.workoutWithSets.observeAsState(emptyList())

    val age by trainingViewModel.age.observeAsState(initial = 25)
    val time by trainingViewModel.time.observeAsState(initial = 25)
    val injuries by trainingViewModel.injure.observeAsState("ninguna")
    val sex by trainingViewModel.sex.observeAsState(1)

    val language by trainingViewModel.language.observeAsState(1)
    val target by trainingViewModel.target.observeAsState(0)
    val difficulty by trainingViewModel.difficulty.observeAsState(2)

    var muscles by remember { mutableStateOf(listOf(50, 31)) }

    val currentUserData = UserDataModel(
        sex = sex,
        age = age,
        target = target,
        muscles = muscles,
        difficulty = difficulty,
        time = time,
        injuries = injuries,
        language = language
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFF3F2F7))
            .padding(horizontal = 8.dp)
    ) {

        ConfigUserWorkout(Modifier, trainingViewModel, age, time, injuries)
        Spacer(modifier = Modifier.height(8.dp))

        SelectSex(trainingViewModel)
        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuLanguage(trainingViewModel)
        Spacer(modifier = Modifier.height(8.dp))

        UpdateTraining(Modifier, trainingViewModel, currentUserData)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            Modifier
                .fillMaxSize()
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
    currentAge: Int, currentTime: Int, currentInjure: String
) {

    var ageInt by remember { mutableIntStateOf(0) }
    var ageString by remember { mutableStateOf("") }
    var timeInt by remember { mutableIntStateOf(0) }
    var timeString by remember { mutableStateOf("") }
    var injure by remember { mutableStateOf("ninguna") }

    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {
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
                                Color.Blue
                            else
                                Color.Black
                            )
                )
            }
        )
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
                                Color.Blue
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
                                Color.Blue
                            else
                                Color.Black
                            )
                )
            }
        )
    }
}

@Composable
fun SelectSex(trainingViewModel: TrainingViewModel) {

    var selectColor by remember { mutableStateOf(true) }

    Row(
        Modifier
            .fillMaxWidth()
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    selectColor = true
                    trainingViewModel.onChangeSex(1)
                },
            colors = if (selectColor) {
                CardDefaults.cardColors(Color(0xF3365CA4))
            } else CardDefaults.cardColors(Color(0xF396A6CB))
        ) {
            Icon(
                painter = painterResource(id = com.cursokotlin.entrenamientosconroom.R.drawable.ic_male),
                contentDescription = "",
                Modifier
                    .size(40.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        Spacer(Modifier.size(8.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    selectColor = false
                    trainingViewModel.onChangeSex(0)
                },
            colors =
            if (selectColor == false) {
                CardDefaults.cardColors(Color(0xF3365CA4))
            } else CardDefaults.cardColors(Color(0xF396A6CB))
        ) {
            Icon(
                painter = painterResource(com.cursokotlin.entrenamientosconroom.R.drawable.ic_female),
                contentDescription = "btn_plus",
                Modifier
                    .size(40.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun UpdateTraining(
    modifier: Modifier = Modifier,
    trainingViewModel: TrainingViewModel,
    currentUserData: UserDataModel
) {
    var lastUserData = remember {
        mutableStateOf(UserDataModel(0, 0, 0, listOf(), 0, 0, "", 0))
    }
    Card(
        modifier = Modifier
            .size(width = 170.dp, height = 40.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color(0xF32C4A9F)),
        onClick = {
            if (currentUserData != lastUserData.value) {
                lastUserData.value = currentUserData
                trainingViewModel.loadWorkout(currentUserData)
            }
        },
        enabled = currentUserData != lastUserData.value,
    )
    {
        Text(
            text = "Update Training",
            Modifier
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun DropdownMenuLanguage(trainingViewModel: TrainingViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("English", "Spanish")
    var selectedLanguage by remember { mutableStateOf(languages[0]) }
    val selectedIndex = languages.indexOf(selectedLanguage)
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .clickable { expanded = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(Color(0xFFFDFDFD)),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(Modifier.padding(4.dp), horizontalArrangement = Arrangement.Center) {
                Spacer(Modifier.size(20.dp))
                Icon(
                    painter = painterResource(id = com.cursokotlin.entrenamientosconroom.R.drawable.ic_language),
                    contentDescription = "",
                    Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = selectedLanguage,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            languages.forEach {
                Divider(
                    thickness = 0.5.dp,
                    color = Color(0xFF868686),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                DropdownMenuItem(
                    text = { Text(text = it, fontSize = 16.sp) },
                    onClick = {
                        selectedLanguage = it
                        expanded = false
                        trainingViewModel.onChangeLanguage(selectedIndex)
                        Log.d("DropdownMenuLanguage", "Language selected: $selectedLanguage")
                        Log.d("DropdownMenuLanguage", "Index selected: $selectedIndex")
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Color(color = 0xFF545454),
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                )
                Divider(
                    thickness = 0.5.dp,
                    color = Color(0xFF868686),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}




