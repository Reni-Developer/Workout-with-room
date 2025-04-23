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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cursokotlin.entrenamientosconroom.data.bd.WorkoutWithSetsAndExercises
import com.cursokotlin.entrenamientosconroom.data.networkAPI.UserDataModel
import com.cursokotlin.entrenamientosconroom.ui.viewmodel.TrainingViewModel

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
            TextFieldWorkout(Modifier.weight(1f), trainingViewModel, age, time, injuries)
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

        TrainingSpacer(Modifier.weight(0.9f), workoutWithSetsAndExercises)
        Spacer(modifier = Modifier.height(8.dp))

        UpdateTraining(Modifier.weight(0.12f), trainingViewModel, currentUserData)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ConfigMuscles(modifier: Modifier, trainingViewModel: TrainingViewModel) {

    val muscles by trainingViewModel.musclesById.observeAsState(listOf())

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
                            trainingViewModel.updateMuscleSelection(index, it)
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
fun TextFieldWorkout(
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
        TextField(
            value = ageString,
            onValueChange = {
                ageString = it
                if (ageString != "" && ageString.toIntOrNull() != null) ageInt = ageString.toInt()
            },
            Modifier.weight(1f),
            label = {
                Text(text = "Your Age")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                if (ageInt > 9 && ageInt < 100) trainingViewModel.onChangeAge(ageInt)
            }),
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
                    tint = (if (ageInt == currentAge && ageInt != 0) Color.Blue
                    else Color.Black)
                )
            },
            colors = TextFieldDefaults.colors(
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
            Modifier.weight(1f),
            label = { Text(text = "Workout Time") },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                if (timeInt > 9 && timeInt < 121) trainingViewModel.onChangeTime(timeInt)
            }),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        if (timeInt > 9 && timeInt < 121) trainingViewModel.onChangeTime(timeInt)
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
            Modifier.weight(1f),
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
                CardDefaults.cardColors(Color(0xFFB2B2B2))
            } else CardDefaults.cardColors(Color(0xFFFBFBFB))
        ) {
            Icon(
                painter = painterResource(id = com.cursokotlin.entrenamientosconroom.R.drawable.ic_male),
                contentDescription = "",
                Modifier
                    .size(40.dp)
                    .align(Alignment.CenterHorizontally),
                tint = if (selectColor) Color.Blue else Color.Black
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
                CardDefaults.cardColors(Color(0xFFB2B2B2))
            } else CardDefaults.cardColors(Color(0xFFFBFBFB))
        ) {
            Icon(
                painter = painterResource(com.cursokotlin.entrenamientosconroom.R.drawable.ic_female),
                contentDescription = "female",
                Modifier
                    .size(40.dp)
                    .align(Alignment.CenterHorizontally),
                tint = if (selectColor == false) Color(0xDDE040FB) else Color.Black
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
Log.d("UpdateTraining", "isLoading = $isLoading")

    val enable = currentUserData != lastUserData.value

        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min), // Asegura que solo crece lo necesario
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Color(0xFF485C91)),
            onClick = {
                if (currentUserData != lastUserData.value) {
                    lastUserData.value = currentUserData
                    trainingViewModel.loadWorkout(currentUserData)
                }
            },
            enabled = enable
        ) {
            if (isLoading) {
                Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
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
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Icon(
                    painter = painterResource(id = com.cursokotlin.entrenamientosconroom.R.drawable.ic_fitness),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp),
                    tint =
                    if (enable)
                    Color(0xFFEEFF41)
                    else Color(0xFFFFFFFF)
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
                        .align(Alignment.CenterVertically),
                    tint = when (selected) {
                        "Spanish" -> Color.Blue
                        "English" -> Color.Green
                        "Gain muscle" -> Color.Red
                        "Lose weight" -> Color.Green
                        "Maintain or improve health" -> Color.Yellow
                        "Easy" -> Color.Yellow
                        "Medium" -> Color.Green
                        "Hard" -> Color.Red
                        else -> Color.Black
                    }
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
                        Log.d("DropdownMenu", "option selected: $options")
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
                    color = Color(0xFF485C91),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    text = "Entrenamiento: ${workoutWithSets.workout.name} "
                )
                Text(
                    color = Color(0xFF424657),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.End,
                    text = "${workoutWithSets.workout.coach_explanation} "
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider(
                    thickness = 1.dp,
                    color = Color(0xFF485C91),
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
                        text = "Set No.${setWithExercise.set.order_set_id + 1}"
                    )
                    Text(
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        text = "${setWithExercise.set.rounds} Rounds"
                    )
                    Spacer(modifier = Modifier.size(8.dp))

                    setWithExercise.exercises.forEach { exercise ->
                        Text(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            text = "Exercise: ${exercise.order_exercise_id + 1} "
                        )
                        Text(
                            color = Color.Black,
                            text = when (exercise.muscle_id) {
                                50 -> "Ejercicios para: Pecho"
                                31 -> "Ejercicios para: Tríceps"
                                90 -> "Ejercicios para: Hombros"
                                10 -> "Ejercicios para: Espalda"
                                30 -> "Ejercicios para: Bíceps"
                                60 -> "Ejercicios para: Antebrazos"
                                20 -> "Ejercicios para: Trapecios"
                                11 -> "Ejercicios para: Cuádriceps"
                                70 -> "Ejercicios para: Glúteos"
                                40 -> "Ejercicios para: Isquiotibiales"
                                21 -> "Ejercicios para: Pantorrillas"
                                51 -> "Ejercicios para: Aductores"
                                61 -> "Ejercicios para: Abductores"
                                80 -> "Ejercicios para: Abdomen"
                                41 -> "Ejercicios para: Espalda baja"
                                else -> "Ejercicio desconocido"
                            }
                        )
                        Text(color = Color.Black, text = exercise.name)
                        Text(color = Color.Black, text = "Reps ${exercise.reps} ")
                        Text(
                            color = Color.Black,
                            text = when (exercise.movement_id) {
                                803 -> "Movimiento de: Flexión del abdomen superior"
                                116 -> "Movimiento de: Prensa"
                                902 -> "Movimiento de: Elevación frontal de hombros"
                                200 -> "Movimiento de: Encogimientos de trapecio"
                                115 -> "Movimiento de: Extensión de rodilla"
                                102 -> "Movimiento de: Jalón en vertical"
                                805 -> "Movimiento de: Flexión lateral"
                                501 -> "Movimiento de: Empuje recto"
                                110 -> "Movimiento de: Subida al cajón"
                                113 -> "Movimiento de: Levantamiento de cadera"
                                315 -> "Movimiento de: Fondos"
                                301 -> "Movimiento de: Curls de martillo o invertido"
                                504 -> "Movimiento de: Aperturas"
                                802 -> "Movimiento de: Flexión abdominal completa"
                                111 -> "Movimiento de: Curl de isquiotibiales"
                                103 -> "Movimiento de: Extensión de codo en plano bajo"
                                311 -> "Movimiento de: Extensión de codo sobre la cabeza"
                                502 -> "Movimiento de: Empuje inclinado"
                                505 -> "Movimiento de: Pullover"
                                314 -> "Movimiento de: Empuje recto con agarre cerrado"
                                210 -> "Movimiento de: Elevación de talones"
                                610 -> "Movimiento de: Abducción de cadera"
                                410 -> "Movimiento de: Extensión de espalda"
                                201 -> "Movimiento de: Remo vertical"
                                101 -> "Movimiento de: Jalón en horizontal"
                                117 -> "Movimiento de: Zancada"
                                801 -> "Movimiento de: Estabilidad del core"
                                313 -> "Movimiento de: Extensión de codo hacia el frente"
                                806 -> "Movimiento de: Flexión de abdomen inferior"
                                118 -> "Movimiento de: Zancada estática"
                                300 -> "Movimiento de: Curl supinado"
                                804 -> "Movimiento de: Rotación de tronco"
                                114 -> "Movimiento de: Extensión de cadera"
                                119 -> "Movimiento de: Sentadillas"
                                510 -> "Movimiento de: Aducción de cadera"
                                903 -> "Movimiento de: Elevación lateral de hombros"
                                901 -> "Movimiento de: Elevación de deltoides posterior"
                                904 -> "Movimiento de: Empuje vertical"
                                503 -> "Movimiento de: Empuje declinado"
                                112 -> "Movimiento de: Bisagra de cadera"
                                302 -> "Movimiento de: Curls de aislamiento"
                                600 -> "Movimiento de: Curl de muñeca"
                                312 -> "Movimiento de: Extensión de codo en plano bajo"
                                else -> "Ejercicio desconocido"
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}



