@file:Suppress("DEPRECATION")

package com.cursokotlin.entrenamientosconroom.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutWithSetsAndExercises
import com.cursokotlin.entrenamientosconroom.data.networkAPI.UserDataModel
import com.cursokotlin.entrenamientosconroom.viewmodel.TrainingViewModel
import kotlin.math.log

@Composable
fun TrainingScreen(modifier: Modifier = Modifier, trainingViewModel: TrainingViewModel) {

    val workoutWithSetsAndExercises by trainingViewModel.workoutWithSets.observeAsState(emptyList())


    val age by trainingViewModel.age.observeAsState(initial = 0)
    val time by trainingViewModel.time.observeAsState(initial = 0)
    val injuries by trainingViewModel.injure.observeAsState("ninguna")
    val sex by trainingViewModel.sex.observeAsState(0)
    val language by trainingViewModel.language.observeAsState(0)
    val target by trainingViewModel.target.observeAsState(0)
    val difficulty by trainingViewModel.difficulty.observeAsState(0)
    val muscles by trainingViewModel.muscles.observeAsState(listOf())

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

    val languages = listOf("Spanish", "English")
    val languageIcon = com.cursokotlin.entrenamientosconroom.R.drawable.ic_translate
    val targets = listOf("Gain muscle", "Lose weight", "Maintain or improve health")
    val targetsIcon = com.cursokotlin.entrenamientosconroom.R.drawable.ic_assignment
    val difficulties = listOf("Easy", "Medium", "Hard")
    val difficultiesIcon = com.cursokotlin.entrenamientosconroom.R.drawable.ic_difficulty

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFF3F2F7))
            .padding(horizontal = 8.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Row(Modifier.weight(0.35f)) {
            ConfigUserWorkout(Modifier.weight(1f), trainingViewModel, age, time, injuries)
            Spacer(Modifier.width(8.dp))
            ConfigMuscles(Modifier.weight(1f), trainingViewModel)
        }
        Spacer(Modifier.height(8.dp))

        SelectSex(trainingViewModel)
        Spacer(Modifier.height(8.dp))

        SelectDropdownMenu(Modifier, languages, languageIcon, trainingViewModel)
        Spacer(Modifier.height(8.dp))

        SelectDropdownMenu(Modifier, targets, targetsIcon, trainingViewModel)
        Spacer(Modifier.height(8.dp))

        SelectDropdownMenu(Modifier, difficulties, difficultiesIcon, trainingViewModel)
        Spacer(Modifier.height(8.dp))

        TrainingSpacer(Modifier.weight(1f), workoutWithSetsAndExercises)
        Spacer(modifier = Modifier.height(8.dp))

        UpdateTraining(Modifier.fillMaxWidth(), trainingViewModel, currentUserData)
        Spacer(modifier = Modifier.height(8.dp))

    }
}

@Composable
fun ConfigMuscles(modifier: Modifier, trainingViewModel: TrainingViewModel) {

    var muscles = remember {
        mutableStateListOf(
            CheckInfo("Pecho", false, 50),
            CheckInfo("Tríceps", false, 31),
            CheckInfo("Hombros", false, 90),
            CheckInfo("Espalda", false, 10),
            CheckInfo("Bíceps", false, 30),
            CheckInfo("Antebrazos", false, 60),
            CheckInfo("Trapecios", false, 20),
            CheckInfo("Cuádriceps", false, 11),
            CheckInfo("Glúteos", false, 70),
            CheckInfo("Isquiotibiales", false, 40),
            CheckInfo("Pantorrillas", false, 21),
            CheckInfo("Aductores", false, 51),
            CheckInfo("Abductores", false, 61),
            CheckInfo("Abdomen", false, 80),
            CheckInfo("Espalda baja", false, 41)
        )
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color(0xFFFBFBFB))
    ) {
        LazyColumn {
            itemsIndexed(muscles) { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = item.selected,
                        onCheckedChange = {
                            muscles[index] = item.copy(selected = it)
                            var selectedMuscles = muscles.filter { it.selected }.mapNotNull { it.id }
                            trainingViewModel.onChangeMuscles(selectedMuscles)
                        })
                    Text(text = item.muscle)
                }
            }
        }
    }
}

data class CheckInfo(
    val muscle: String,
    val selected: Boolean,
    val id: Int? = null
)


@Composable
fun ConfigUserWorkout(
    modifier: Modifier = Modifier,
    trainingViewModel: TrainingViewModel,
    currentAge: Int,
    currentTime: Int,
    currentInjure: String
) {
    var ageInt by remember { mutableIntStateOf(0) }
    var ageString by remember { mutableStateOf("") }
    var timeInt by remember { mutableIntStateOf(0) }
    var timeString by remember { mutableStateOf("") }
    var injure by remember { mutableStateOf("ninguna") }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color(0xFFFBFBFB))
    ) {
        TextField(value = ageString, onValueChange = {
            ageString = it
            if (ageString != "" && ageString.toIntOrNull() != null) ageInt = ageString.toInt()
        }, label = { Text(text = "Your Age") }, keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
        ), keyboardActions = KeyboardActions(onDone = {
            if (ageInt > 9 && ageInt < 100) trainingViewModel.onChangeAge(ageInt)
        }), singleLine = true, maxLines = 1, trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "",
                modifier = Modifier.clickable {
                    if (ageInt > 9 && ageInt < 100) trainingViewModel.onChangeAge(ageInt)
                },
                tint = (if (ageInt == currentAge && ageInt != 0) Color.Blue
                else Color.Black)
            )
        }, colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFC9C9C9),
            unfocusedContainerColor = Color(0xFFFBFBFB)
        )
        )

        TextField(
            value = timeString,
            onValueChange = {
                timeString = it
                if (timeString != "" && timeString.toIntOrNull() != null) timeInt =
                    timeString.toInt()
            },
            label = { Text(text = "Workout Time") },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                if (timeInt > 9 && timeInt < 120) trainingViewModel.onChangeTime(timeInt)
            }),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        if (timeInt > 9 && timeInt < 120) trainingViewModel.onChangeTime(timeInt)
                    },
                    tint = if (timeInt == currentTime && timeInt != 0) Color.Blue
                    else Color.Black
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFC9C9C9),
                unfocusedContainerColor = Color(0xFFFBFBFB)
            )
        )
        TextField(
            value = injure,
            onValueChange = { injure = it },
            label = { Text(text = "Injure") },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                if (injure != "") trainingViewModel.onChangeInjure(injure)
            }),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        if (injure != "") trainingViewModel.onChangeInjure(injure)
                    },
                    tint = (if (injure == currentInjure) Color.Blue
                    else Color.Black)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFC9C9C9),
                unfocusedContainerColor = Color(0xFFFBFBFB)
            )
        )
    }
}

@Composable
fun SelectSex(trainingViewModel: TrainingViewModel) {

    var selectColor by remember { mutableStateOf(true) }

    Row(
        Modifier.fillMaxWidth()
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    selectColor = true
                    trainingViewModel.onChangeSex(1)
                }, elevation = CardDefaults.cardElevation(8.dp), colors = if (selectColor) {
                CardDefaults.cardColors(Color(0xFF7C7C7C))
            } else CardDefaults.cardColors(Color(0xFFFBFBFB))
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
            elevation = CardDefaults.cardElevation(8.dp),
            colors = if (selectColor == false) {
                CardDefaults.cardColors(Color(0xFF7C7C7C))
            } else CardDefaults.cardColors(Color(0xFFFBFBFB))
        ) {
            Icon(
                painter = painterResource(com.cursokotlin.entrenamientosconroom.R.drawable.ic_female),
                contentDescription = "female",
                Modifier
                    .size(40.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun UpdateTraining(
    modifier: Modifier, trainingViewModel: TrainingViewModel, currentUserData: UserDataModel
) {
    var lastUserData = remember {
        mutableStateOf(UserDataModel(0, 0, 0, listOf(), 0, 0, "", 0))
    }
    val isLoading by trainingViewModel.isLoading.observeAsState(false)

    if (isLoading) {
        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min), // Asegura que solo crece lo necesario
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Color(0xFFFBFBFB)),
            onClick = {
                if (currentUserData != lastUserData.value) {
                    lastUserData.value = currentUserData
                    trainingViewModel.loadWorkout(currentUserData)
                }
            },
            enabled = (currentUserData != lastUserData.value)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(Modifier.width(8.dp))
                Text(
                    text = "UpdateTraining",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    painter = painterResource(id = com.cursokotlin.entrenamientosconroom.R.drawable.ic_fitness),
                    contentDescription = null,
                    modifier = modifier.size(35.dp)
                )
            }
        }
    }
}

@Composable
fun SelectDropdownMenu(
    modifier: Modifier, options: List<String>, icon: Int, trainingViewModel: TrainingViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val options = options
    var selected by remember { mutableStateOf(options[0]) }
    val selectedIndex = options.indexOf(selected)

    Column(modifier = modifier) {
        Card(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .clickable { expanded = true },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(Color(0xFFFDFDFD)),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = modifier.padding(4.dp), horizontalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.size(20.dp))
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "",
                    Modifier
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = selected,
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
            options.forEach {
                Divider(
                    thickness = 0.5.dp,
                    color = Color(0xFF868686),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                DropdownMenuItem(
                    text = { Text(text = it, fontSize = 16.sp) },
                    onClick = {
                        selected = it
                        expanded = false
                        when (icon) {
                            com.cursokotlin.entrenamientosconroom.R.drawable.ic_translate -> trainingViewModel.onChangeLanguage(
                                selectedIndex
                            )

                            com.cursokotlin.entrenamientosconroom.R.drawable.ic_assignment -> trainingViewModel.onChangeTarget(
                                selectedIndex
                            )

                            com.cursokotlin.entrenamientosconroom.R.drawable.ic_difficulty -> trainingViewModel.onChangeDifficulty(
                                selectedIndex
                            )
                        }
                        Log.d("DropdownMenu", "Index selected: $selected")
                        Log.d("DropdownMenu", "Index selected: $selectedIndex")
                    }, colors = MenuDefaults.itemColors(
                        textColor = Color(color = 0xFF545454),
                    ), contentPadding = PaddingValues(horizontal = 16.dp)
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

@Composable
fun TrainingSpacer(
    modifier: Modifier, workoutWithSetsAndExercises: List<WorkoutWithSetsAndExercises>
) {
    Card(
        modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(Color(0xFFFBFBFB))
    ) {
        LazyColumn(contentPadding = PaddingValues(8.dp)) {
            items(workoutWithSetsAndExercises, key = { it.workout.workoutId }) { workoutWithSets ->
                Text(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    text = "Entrenamiento: ${workoutWithSets.workout.name} "
                )
                Text(
                    color = Color.Black,
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
                        Text(color = Color.Black, text = "${exercise.order_exercise_id} ")
                        Text(color = Color.Black, text = "Exercise: ${exercise.name}")
                        Text(color = Color.Black, text = "Reps ${exercise.reps} ")
                        Text(color = Color.Black, text = "movement ${exercise.movement_id}")
                        Text(color = Color.Black, text = "muscle ${exercise.muscle_id}")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}



